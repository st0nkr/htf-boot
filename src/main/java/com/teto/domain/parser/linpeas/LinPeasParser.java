package com.teto.domain.parser.linpeas;

import com.teto.IFile;
import com.teto.command.Context;
import com.teto.domain.cve.CVE;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.meta.Tag;
import com.teto.domain.port.ServicePort;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinPeasParser implements IFile {

    private static final Pattern ANSI_PATTERN = Pattern.compile("\\u001B\\[[;\\d]*[ -/]*[@-~]|\\x1B\\[[0-9;]*[a-zA-Z]");
    private static final Pattern BOX_TITLE_PATTERN = Pattern.compile("═+\\s*╣\\s*([^╠]+?)\\s*╠\\s*═+");
    private static final Pattern SUBSECTION_2_PATTERN = Pattern.compile("^╔═+╣\\s*([^(\n\r]+?)(?:\\s*\\((.*?)\\))?$");
    private static final Pattern SUBSECTION_3_PATTERN = Pattern.compile("^═+╣\\s*([^(\n\r]+?)(?:\\s*\\((.*?)\\))?$");
    private static final Pattern CVE_PATTERN = Pattern.compile("(?i)(CVE-\\d{4}-\\d{4,7})");
    private static final Pattern LES_PATTERN = Pattern.compile("\\[\\+\\]\\s*\\[?(CVE-\\d{4}-\\d{4,7})\\]?\\s*(.*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern OS_PATTERN = Pattern.compile("^OS:\\s*(.*)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern USER_GROUPS_PATTERN = Pattern.compile("uid=(\\d+)\\(([^)]+)\\)\\s*gid=(\\d+)\\(([^)]+)\\)\\s*groups=(.*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern HOSTNAME_PATTERN = Pattern.compile("^Hostname:\\s*(\\S+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SUDO_VERSION_PATTERN = Pattern.compile("Sudo version\\s+([0-9a-zA-Z._-]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SUDO_RULE_PATTERN = Pattern.compile("\\(([^)]+)\\)\\s*(?:(NOPASSWD:))?\\s*(\\S.*)");
    private static final Pattern LISTENING_PORT_PATTERN = Pattern.compile("(?i)^(tcp|udp|tcp6|udp6)\\s+\\d+\\s+\\d+\\s+([0-9a-fA-F.:*]+):(\\d+)\\s+([0-9a-fA-F.:*]+):(?:\\d+|\\*)\\s*(LISTEN|ESTABLISHED)?");
    private static final Pattern IP_PATTERN = Pattern.compile("inet\\s+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(?:/\\d+)?");
    private static final Pattern HASH_PATTERN = Pattern.compile("(\\$[156y]\\$[a-zA-Z0-9./]+\\$[a-zA-Z0-9./]+|\\$2[abxy]?\\$\\d{2}\\$[a-zA-Z0-9./]+)");
    private static final Pattern SSH_KEY_PATTERN = Pattern.compile("-----BEGIN (?:[A-Z0-9_-]+ )?PRIVATE KEY-----");
    private static final Pattern PASSWD_USER_PATTERN = Pattern.compile("^([a-zA-Z0-9_.-]+):[^:]*:(\\d+):(\\d+):([^:]*):([^:]*):([^:]*)$");

    public LinPeasResult parse(Context ctx, String fileName) {
        Optional<List<String>> lines = readFileAsLines(fileName);
        if (lines.isPresent()) {
            LinPeasResult result = parse(lines.get());
            if (ctx != null) {
                try {
                    ctx.stash(Tag.LinPeas, result);
                } catch (Exception ignored) {
                }
            }
            return result;
        }
        return new LinPeasResult();
    }

    public String removeRedBold(String text) {
        if(text.contains("[1;31m")) {
            return text.replace("[1;31m","");
        }
        return text;
    }
    public LinPeasResult parse(Context ctx, Target target, String fileName) {
        LinPeasResult result = parse(ctx, fileName);
        if (ctx != null && target != null) {
            ScannedTargets st = result.toScannedTargets(target);
            ScannedTargets existing = ctx.fetch(ScannedTargets.class);
            if (existing != null) {
                existing.add(st);
            } else {
                ctx.stash(st);
            }
        }
        return result;
    }

    public LinPeasResult parseFile(File file) {
        if (file == null) {
            return new LinPeasResult();
        }
        Optional<List<String>> lines = readFileAsLines(file);
        return lines.map(this::parse).orElseGet(LinPeasResult::new);
    }

    public LinPeasResult parseFile(String filePath) {
        if (filePath == null) {
            return new LinPeasResult();
        }
        Optional<List<String>> lines = readFileAsLines(filePath);
        return lines.map(this::parse).orElseGet(LinPeasResult::new);
    }

    public LinPeasResult parse(String content) {
        if (content == null || content.isBlank()) {
            return new LinPeasResult();
        }
        String[] split = content.split("\r?\n");
        return parse(Arrays.asList(split));
    }

    public LinPeasResult parse(List<String> rawLines) {
        LinPeasResult result = new LinPeasResult();
        if (rawLines == null || rawLines.isEmpty()) {
            return result;
        }

        LinPeasSection currentSection = null;
        LinPeasSubsection currentSubsection = null;

        Set<String> seenCves = new HashSet<>();
        Set<String> seenExploits = new HashSet<>();
        Set<String> seenSuids = new HashSet<>();
        Set<String> seenSgids = new HashSet<>();
        Set<String> seenPorts = new HashSet<>();
        Set<String> seenUsers = new HashSet<>();
        Set<String> seenSudoRules = new HashSet<>();

        for (String rawLine : rawLines) {
            // Red bold control characters are getting through
            rawLine = removeRedBold(rawLine);
            rawLine = replaceTabs(rawLine, " ");
            LinPeasSeverity severity = detectSeverity(rawLine);
            String cleanLine = stripAnsi(rawLine).trim();

            if (cleanLine.isEmpty()) {
                continue;
            }

            // Check if this is a main section header (e.g. ═════╣ Basic information ╠═════)
            Matcher boxTitleMatcher = BOX_TITLE_PATTERN.matcher(cleanLine);
            if (boxTitleMatcher.find()) {
                String sectionName = boxTitleMatcher.group(1).trim();
                currentSection = new LinPeasSection(sectionName);
                result.getSections().add(currentSection);
                currentSubsection = null;
                continue;
            }

            // Check if this is a 2-level subsection header (╔══════════╣ Title (Tag))
            Matcher sub2Matcher = SUBSECTION_2_PATTERN.matcher(cleanLine);
            if (sub2Matcher.find()) {
                String title = sub2Matcher.group(1).trim();
                String tag = sub2Matcher.group(2) != null ? sub2Matcher.group(2).trim() : null;
                currentSubsection = new LinPeasSubsection(title, tag);
                if (currentSection == null) {
                    currentSection = new LinPeasSection("General");
                    result.getSections().add(currentSection);
                }
                currentSection.getSubsections().add(currentSubsection);
                continue;
            }

            // Check if this is a 3-level subsection header (══╣ Title (Tag))
            Matcher sub3Matcher = SUBSECTION_3_PATTERN.matcher(cleanLine);
            if (sub3Matcher.find()) {
                String title = sub3Matcher.group(1).trim();
                String tag = sub3Matcher.group(2) != null ? sub3Matcher.group(2).trim() : null;
                currentSubsection = new LinPeasSubsection(title, tag);
                if (currentSection == null) {
                    currentSection = new LinPeasSection("General");
                    result.getSections().add(currentSection);
                }
                currentSection.getSubsections().add(currentSubsection);
                continue;
            }

            // Store raw line in section / subsection
            if (currentSection != null) {
                currentSection.getLines().add(cleanLine);
            }
            if (currentSubsection != null) {
                currentSubsection.getLines().add(cleanLine);
            }

            // Extract structured data from line
            parseLineData(cleanLine, rawLine, severity, currentSection, currentSubsection, result,
                    seenCves, seenExploits, seenSuids, seenSgids, seenPorts, seenUsers, seenSudoRules);
        }

        return result;
    }

    private String replaceTabs(String rawLine, String replace) {
        return rawLine.replace("\t", replace);
    }

    private void parseLineData(String cleanLine, String rawLine, LinPeasSeverity severity,
                               LinPeasSection currentSection, LinPeasSubsection currentSubsection,
                               LinPeasResult result, Set<String> seenCves, Set<String> seenExploits,
                               Set<String> seenSuids, Set<String> seenSgids, Set<String> seenPorts,
                               Set<String> seenUsers, Set<String> seenSudoRules) {

        String sectionName = currentSection != null ? currentSection.getName() : "";
        String subTitle = currentSubsection != null ? currentSubsection.getTitle() : "";
        String mitreTag = currentSubsection != null ? currentSubsection.getTag() : null;

        // 1. OS & Kernel & Hostname & User
        Matcher osMatcher = OS_PATTERN.matcher(cleanLine);
        if (osMatcher.find()) {
            String osDetails = osMatcher.group(1).trim();
            result.setOs(osDetails);
            parseOsDetails(osDetails, result);
        }

        if (cleanLine.startsWith("Linux version ") && result.getKernel() == null) {
            parseOsDetails(cleanLine, result);
        }

        Matcher userGroupsMatcher = USER_GROUPS_PATTERN.matcher(cleanLine);
        if (userGroupsMatcher.find()) {
            result.setCurrentUid(Integer.parseInt(userGroupsMatcher.group(1)));
            result.setCurrentUser(userGroupsMatcher.group(2));
            result.setCurrentGid(Integer.parseInt(userGroupsMatcher.group(3)));
            result.setCurrentGroup(userGroupsMatcher.group(4));

            String groupsStr = userGroupsMatcher.group(5);
            List<String> groupsList = parseGroups(groupsStr);
            result.setGroups(groupsList);
        }

        Matcher hostnameMatcher = HOSTNAME_PATTERN.matcher(cleanLine);
        if (hostnameMatcher.find()) {
            result.setHostname(hostnameMatcher.group(1).trim());
        }

        // 2. Sudo version & rules
        Matcher sudoVerMatcher = SUDO_VERSION_PATTERN.matcher(cleanLine);
        if (sudoVerMatcher.find()) {
            result.setSudoVersion(sudoVerMatcher.group(1).trim());
        }

        if (subTitle.toLowerCase().contains("sudo") || cleanLine.contains("NOPASSWD") || cleanLine.contains("(root)") || cleanLine.contains("(ALL")) {
            Matcher sudoRuleMatcher = SUDO_RULE_PATTERN.matcher(cleanLine);
            if (sudoRuleMatcher.find()) {
                String rule = cleanLine.trim();
                if (seenSudoRules.add(rule)) {
                    result.getSudoRules().add(rule);
                    addFinding(result, currentSubsection, "Sudo", "Sudo Privilege", rule,
                            rule.contains("NOPASSWD") ? LinPeasSeverity.RED_YELLOW : LinPeasSeverity.RED, rawLine, mitreTag);
                }
            } else if (cleanLine.contains("NOPASSWD") || cleanLine.contains("(ALL : ALL)")) {
                if (seenSudoRules.add(cleanLine)) {
                    result.getSudoRules().add(cleanLine);
                    addFinding(result, currentSubsection, "Sudo", "Sudo Privilege", cleanLine,
                            cleanLine.contains("NOPASSWD") ? LinPeasSeverity.RED_YELLOW : LinPeasSeverity.RED, rawLine, mitreTag);
                }
            }
        }

        // 3. SUID / SGID Binaries
        if (subTitle.toLowerCase().contains("suid") || subTitle.toLowerCase().contains("sgid")
                || cleanLine.startsWith("-rws") || cleanLine.startsWith("-r-s") || cleanLine.startsWith("---s")) {
            extractSuidSgid(cleanLine, rawLine, severity, subTitle, result, currentSubsection, seenSuids, seenSgids, mitreTag);
        }

        // 4. Listening ports / sockets
        Matcher portMatcher = LISTENING_PORT_PATTERN.matcher(cleanLine);
        if (portMatcher.find()) {
            String proto = portMatcher.group(1).toLowerCase();
            String ip = portMatcher.group(2);
            int port = Integer.parseInt(portMatcher.group(3));
            String state = portMatcher.group(5) != null && !portMatcher.group(5).isEmpty() ? portMatcher.group(5) : "LISTEN";

            String portKey = proto + ":" + ip + ":" + port;
            if (seenPorts.add(portKey)) {
                result.getListeningPorts().add(portKey);

                ServicePort sp = new ServicePort();
                sp.setPortNumber((long) port);
                sp.setProtocol(proto);
                sp.setIpAddress(ip);
                sp.setPortState(state);
                sp.setStatus(state);
                sp.setProvenance(Provenance.LinPeas.name());
                sp.setName("Port-" + port);
                result.getServicePorts().add(sp);
            }
        }

        // 5. IP Addresses
        Matcher ipMatcher = IP_PATTERN.matcher(cleanLine);
        while (ipMatcher.find()) {
            String ip = ipMatcher.group(1);
            if (!ip.startsWith("127.") && !result.getIpAddresses().contains(ip)) {
                result.getIpAddresses().add(ip);
            }
        }

        // 6. Cron Jobs & Timers
        if (subTitle.toLowerCase().contains("cron") || sectionName.toLowerCase().contains("cron")) {
            if (isCronLine(cleanLine)) {
                if (!result.getCronJobs().contains(cleanLine)) {
                    result.getCronJobs().add(cleanLine);
                    addFinding(result, currentSubsection, "Cron", "Scheduled Cron Job", cleanLine,
                            severity != LinPeasSeverity.INFO ? severity : LinPeasSeverity.YELLOW, rawLine, mitreTag);
                }
            }
        }

        if (subTitle.toLowerCase().contains("timer") || cleanLine.endsWith(".timer")) {
            if (!result.getTimers().contains(cleanLine) && !cleanLine.startsWith("==") && !cleanLine.startsWith("╔═")) {
                result.getTimers().add(cleanLine);
            }
        }

        // 7. Users & Passwords
        Matcher passwdMatcher = PASSWD_USER_PATTERN.matcher(cleanLine);
        if (passwdMatcher.find()) {
            String username = passwdMatcher.group(1);
            int uid = Integer.parseInt(passwdMatcher.group(2));
            int gid = Integer.parseInt(passwdMatcher.group(3));
            String gecos = passwdMatcher.group(4);
            String home = passwdMatcher.group(5);
            String shell = passwdMatcher.group(6);

            if (seenUsers.add(username)) {
                ScannedUser user = new ScannedUser();
                user.setUserName(username);
                user.setUid(uid);
                user.setGid(gid);
                user.setUserInfo(gecos);
                user.setHomeDirectory(home);
                user.setShell(shell);
                user.setProvenance(Provenance.LinPeas.name());
                result.getUsers().add(user);

                if (shell.endsWith("sh") || shell.endsWith("bash") || shell.endsWith("zsh") || shell.endsWith("csh")) {
                    if (!result.getUsersWithConsole().contains(username)) {
                        result.getUsersWithConsole().add(username);
                    }
                }
            }
        }

        if (subTitle.toLowerCase().contains("users with console") || subTitle.toLowerCase().contains("superusers")) {
            if (!cleanLine.startsWith("==") && !cleanLine.startsWith("╔═") && cleanLine.length() < 30 && !cleanLine.contains(" ")) {
                if (seenUsers.add(cleanLine)) {
                    result.getUsersWithConsole().add(cleanLine);
                    ScannedUser u = new ScannedUser();
                    u.setUserName(cleanLine);
                    u.setProvenance(Provenance.LinPeas.name());
                    result.getUsers().add(u);
                }
            }
        }

        // 8. Hashes & Credentials & SSH Keys
        Matcher hashMatcher = HASH_PATTERN.matcher(cleanLine);
        while (hashMatcher.find()) {
            String hash = hashMatcher.group(1);
            if (!result.getPasswordHashes().contains(hash)) {
                result.getPasswordHashes().add(hash);
                addFinding(result, currentSubsection, "Credentials", "Password Hash Found", hash,
                        LinPeasSeverity.RED_YELLOW, rawLine, mitreTag);
            }
        }

        Matcher sshKeyMatcher = SSH_KEY_PATTERN.matcher(cleanLine);
        if (sshKeyMatcher.find()) {
            if (!result.getSshKeys().contains(cleanLine)) {
                result.getSshKeys().add(cleanLine);
                addFinding(result, currentSubsection, "Credentials", "SSH Private Key", cleanLine,
                        LinPeasSeverity.RED_YELLOW, rawLine, mitreTag);
            }
        }

        if (cleanLine.toLowerCase().contains("password") || cleanLine.toLowerCase().contains("passwd")
                || cleanLine.toLowerCase().contains("api_key") || cleanLine.toLowerCase().contains("secret_key")) {
            if (severity == LinPeasSeverity.RED_YELLOW || severity == LinPeasSeverity.RED) {
                if (!result.getCredentials().contains(cleanLine)) {
                    result.getCredentials().add(cleanLine);
                    addFinding(result, currentSubsection, "Credentials", "Potential Credential / Secret", cleanLine,
                            severity, rawLine, mitreTag);
                }
            }
        }

        // 9. CVEs & Exploits
        Matcher lesMatcher = LES_PATTERN.matcher(cleanLine);
        if (lesMatcher.find()) {
            String cveId = lesMatcher.group(1).toUpperCase();
            String title = lesMatcher.group(2).trim();
            if (seenCves.add(cveId)) {
                CVE cve = new CVE();
                cve.setCveId(cveId);
                cve.setDescription(title);
                cve.setProvenance(Provenance.LinPeas);
                result.getCves().add(cve);
            }
            if (seenExploits.add(cveId + ":" + title)) {
                Exploit exploit = new Exploit();
                exploit.setEDB_ID(cveId);
                exploit.setCodes(cveId);
                exploit.setTitle(title.isEmpty() ? cveId : title);
                exploit.setProvenance(Provenance.LinPeas.name());
                result.getExploits().add(exploit);

                addFinding(result, currentSubsection, "Exploit", "Kernel/System Exploit: " + cveId,
                        title.isEmpty() ? cveId : title, LinPeasSeverity.RED_YELLOW, rawLine, mitreTag);
            }
        } else {
            Matcher cveMatcher = CVE_PATTERN.matcher(cleanLine);
            while (cveMatcher.find()) {
                String cveId = cveMatcher.group(1).toUpperCase();
                if (seenCves.add(cveId)) {
                    CVE cve = new CVE();
                    cve.setCveId(cveId);
                    cve.setDescription(cleanLine);
                    cve.setProvenance(Provenance.LinPeas);
                    result.getCves().add(cve);

                    if (seenExploits.add(cveId)) {
                        Exploit exploit = new Exploit();
                        exploit.setEDB_ID(cveId);
                        exploit.setCodes(cveId);
                        exploit.setTitle(cleanLine);
                        exploit.setProvenance(Provenance.LinPeas.name());
                        result.getExploits().add(exploit);
                    }

                    addFinding(result, currentSubsection, "Vulnerability", "Vulnerability: " + cveId,
                            cleanLine, severity != LinPeasSeverity.INFO ? severity : LinPeasSeverity.RED, rawLine, mitreTag);
                }
            }
        }

        // 10. Container & Cloud
        if (cleanLine.toLowerCase().contains("docker") || cleanLine.contains("/.dockerenv")) {
            result.setContainerType("Docker");
        } else if (cleanLine.toLowerCase().contains("kubernetes") || cleanLine.contains("KUBERNETES_PORT")) {
            result.setContainerType("Kubernetes");
        } else if (cleanLine.toLowerCase().contains("lxc")) {
            result.setContainerType("LXC");
        }

        if (cleanLine.toLowerCase().contains("aws") || cleanLine.contains("169.254.169.254")) {
            result.setCloudProvider("AWS");
        } else if (cleanLine.toLowerCase().contains("gcp") || cleanLine.toLowerCase().contains("google cloud")) {
            result.setCloudProvider("GCP");
        } else if (cleanLine.toLowerCase().contains("azure")) {
            result.setCloudProvider("Azure");
        }

        // 11. Highlighted Findings (Red/Yellow or Red)
        if (severity == LinPeasSeverity.RED_YELLOW || severity == LinPeasSeverity.RED) {
            String category = !sectionName.isEmpty() ? sectionName : "General";
            String title = !subTitle.isEmpty() ? subTitle : "PE Vector";
            addFinding(result, currentSubsection, category, title, cleanLine, severity, rawLine, mitreTag);
        }
    }

    private void extractSuidSgid(String cleanLine, String rawLine, LinPeasSeverity severity,
                                 String subTitle, LinPeasResult result, LinPeasSubsection currentSubsection,
                                 Set<String> seenSuids, Set<String> seenSgids, String mitreTag) {
        String[] tokens = cleanLine.split("\\s+");
        String filePath = null;
        for (int i = tokens.length - 1; i >= 0; i--) {
            if (tokens[i].startsWith("/")) {
                filePath = tokens[i];
                break;
            }
        }
        if (filePath == null && cleanLine.startsWith("/")) {
            filePath = cleanLine.split("\\s+")[0];
        }

        if (filePath != null) {
            boolean isSgid = subTitle.toLowerCase().contains("sgid") || cleanLine.contains("sgid");
            if (isSgid) {
                if (seenSgids.add(filePath)) {
                    result.getSgidFiles().add(filePath);
                }
            } else {
                if (seenSuids.add(filePath)) {
                    result.getSuidFiles().add(filePath);
                    if (severity == LinPeasSeverity.RED_YELLOW || severity == LinPeasSeverity.RED || isHighInterestSuid(filePath)) {
                        addFinding(result, currentSubsection, "SUID", "SUID Binary: " + filePath,
                                cleanLine, severity == LinPeasSeverity.RED_YELLOW ? LinPeasSeverity.RED_YELLOW : LinPeasSeverity.RED,
                                rawLine, mitreTag);
                    }
                }
            }
        }
    }

    private boolean isHighInterestSuid(String filePath) {
        String binary = filePath.substring(filePath.lastIndexOf('/') + 1).toLowerCase();
        return Set.of("pkexec", "sudo", "find", "vim", "vi", "nmap", "bash", "sh", "cp", "mv",
                "python", "python3", "perl", "ruby", "lua", "env", "php", "base64", "gdb",
                "tar", "zip", "awk", "sed", "curl", "wget", "less", "more", "nano", "docker").contains(binary);
    }

    private boolean isCronLine(String line) {
        return line.matches("^\\s*([*\\d,-/]+)\\s+([*\\d,-/]+)\\s+([*\\d,-/]+)\\s+([*\\d,-/]+)\\s+([*\\d,-/]+)\\s+(.*)$");
    }

    private void addFinding(LinPeasResult result, LinPeasSubsection currentSubsection,
                            String category, String title, String details, LinPeasSeverity severity,
                            String rawLine, String mitreTechnique) {
        // Prevent duplicate findings with identical category, title and details
        boolean exists = result.getFindings().stream()
                .anyMatch(f -> Objects.equals(f.getCategory(), category)
                        && Objects.equals(f.getTitle(), title)
                        && Objects.equals(f.getDetails(), details));
        if (!exists) {
            LinPeasFinding finding = LinPeasFinding.builder()
                    .category(category)
                    .title(title)
                    .details(details)
                    .severity(severity)
                    .rawLine(rawLine)
                    .mitreTechnique(mitreTechnique)
                    .build();
            result.getFindings().add(finding);
            if (currentSubsection != null) {
                currentSubsection.getFindings().add(finding);
            }
        }
    }

    private List<String> parseGroups(String groupsStr) {
        List<String> list = new ArrayList<>();
        if (groupsStr == null) {
            return list;
        }
        String[] parts = groupsStr.split(",");
        for (String part : parts) {
            part = part.trim();
            int openParen = part.indexOf('(');
            int closeParen = part.indexOf(')');
            if (openParen != -1 && closeParen > openParen) {
                list.add(part.substring(openParen + 1, closeParen));
            } else if (!part.isEmpty()) {
                list.add(part);
            }
        }
        return list;
    }

    private void parseOsDetails(String text, LinPeasResult result) {
        if (text == null) return;

        if (text.contains("Ubuntu")) {
            result.setOsName("Ubuntu");
        } else if (text.contains("Debian")) {
            result.setOsName("Debian");
        } else if (text.contains("Kali")) {
            result.setOsName("Kali Linux");
        } else if (text.contains("CentOS")) {
            result.setOsName("CentOS");
        } else if (text.contains("Red Hat") || text.contains("RHEL")) {
            result.setOsName("Red Hat Enterprise Linux");
        } else if (text.contains("Fedora")) {
            result.setOsName("Fedora");
        } else if (text.contains("Alpine")) {
            result.setOsName("Alpine Linux");
        } else if (text.contains("Arch")) {
            result.setOsName("Arch Linux");
        }

        Pattern kernelPattern = Pattern.compile("Linux\\s+version\\s+([0-9a-zA-Z._+-]+)");
        Matcher km = kernelPattern.matcher(text);
        if (km.find()) {
            result.setKernel(km.group(1));
        }

        if (text.contains("x86_64") || text.contains("amd64")) {
            result.setArchitecture("x86_64");
        } else if (text.contains("aarch64") || text.contains("arm64")) {
            result.setArchitecture("aarch64");
        } else if (text.contains("i686") || text.contains("i386")) {
            result.setArchitecture("x86");
        } else if (text.contains("armv7l")) {
            result.setArchitecture("armv7l");
        }
    }

    public static String stripAnsi(String text) {
        if (text == null) {
            return "";
        }
        return ANSI_PATTERN.matcher(text).replaceAll("");
    }

    public static LinPeasSeverity detectSeverity(String rawLine) {
        if (rawLine == null) {
            return LinPeasSeverity.INFO;
        }

        // Red on yellow background (e.g. 1;31;103m, 1;33;41m, 31;43m, 41;33m, 33;101m)
        if (rawLine.contains("[1;31;103m") || rawLine.contains("[1;33;41m")
                || rawLine.contains("[31;43m") || rawLine.contains("[41;33m")
                || rawLine.contains("[33;101m") || rawLine.contains("[103;31m")
                || rawLine.contains("[41m") && rawLine.contains("[33m")
                || rawLine.contains("[103m") && rawLine.contains("[31m")) {
            return LinPeasSeverity.RED_YELLOW;
        }

        // Red foreground (1;31m or 0;31m or 31m)
        if (rawLine.contains("[1;31m") || rawLine.contains("[0;31m") || rawLine.contains("[31m")) {
            return LinPeasSeverity.RED;
        }

        // Yellow foreground
        if (rawLine.contains("[1;33m") || rawLine.contains("[0;33m") || rawLine.contains("[33m")) {
            return LinPeasSeverity.YELLOW;
        }

        // Cyan foreground
        if (rawLine.contains("[1;36m") || rawLine.contains("[0;36m") || rawLine.contains("[36m")) {
            return LinPeasSeverity.CYAN;
        }

        // Green foreground
        if (rawLine.contains("[1;32m") || rawLine.contains("[0;32m") || rawLine.contains("[32m")) {
            return LinPeasSeverity.GREEN;
        }

        // Magenta foreground
        if (rawLine.contains("[1;35m") || rawLine.contains("[0;35m") || rawLine.contains("[35m")) {
            return LinPeasSeverity.MAGENTA;
        }

        return LinPeasSeverity.INFO;
    }

    public ScannedTargets parseToTargets(Context ctx, Target parent, String fileName) {
        LinPeasResult result = parse(ctx, fileName);
        return result.toScannedTargets(parent);
    }

    public ScannedTargets parseToTargets(Target parent, String content) {
        LinPeasResult result = parse(content);
        return result.toScannedTargets(parent);
    }
}
