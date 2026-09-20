package com.teto.domain.parser.linpeas;

import com.teto.command.Context;
import com.teto.domain.cve.CVE;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.meta.Tag;
import com.teto.domain.port.ServicePort;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.user.ScannedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinPeasParserTest {

    private LinPeasParser parser;

    @BeforeEach
    void setUp() {
        parser = new LinPeasParser();
    }

    @Test
    void testParseBasicInformation() {
        String output = """
                                       ╔═══════════════════╗
        ═══════════════════════════════╣ Basic information ╠═══════════════════════════════
                                       ╚═══════════════════╝
        OS: Linux version 5.10.0-8-amd64 (debian-kernel@lists.debian.org) (gcc-10 (Debian 10.2.1-6) 10.2.1, GNU ld (GNU Binutils for Debian) 2.35.2) #1 SMP Debian 5.10.46-4 (2021-08-03)
        User & Groups: uid=1000(kali) gid=1000(kali) groups=1000(kali),4(adm),24(cdrom),27(sudo),100(users)
        Hostname: test-host
        """;

        LinPeasResult result = parser.parse(output);

        assertNotNull(result);
        assertEquals("test-host", result.getHostname());
        assertEquals("kali", result.getCurrentUser());
        assertEquals(1000, result.getCurrentUid());
        assertEquals(1000, result.getCurrentGid());
        assertEquals("kali", result.getCurrentGroup());
        assertTrue(result.getGroups().contains("kali"));
        assertTrue(result.getGroups().contains("sudo"));
        assertTrue(result.getGroups().contains("adm"));

        assertEquals("5.10.0-8-amd64", result.getKernel());
        assertEquals("Debian", result.getOsName());
        assertEquals("x86_64", result.getArchitecture());
        assertEquals("Linux", result.getOsFamily());

        assertEquals(1, result.getSections().size());
        assertEquals("Basic information", result.getSections().get(0).getName());
    }

    @Test
    void testParseSudoPrivilegesAndVersion() {
        String output = """
        ╔══════════╣ Sudo version
        Sudo version 1.8.31
        
        ╔══════════╣ Sudo -l
        Matching Defaults entries for user on victim:
            env_reset, mail_badpass, secure_path=/usr/local/sbin\\:/usr/local/bin\\:/usr/sbin\\:/usr/bin\\:/sbin\\:/bin
        
        User victim may run the following commands on victim:
            (ALL : ALL) ALL
            (root) NOPASSWD: /usr/bin/find
            (root) NOPASSWD: /usr/bin/python3 /opt/backup.py
        """;

        LinPeasResult result = parser.parse(output);

        assertEquals("1.8.31", result.getSudoVersion());
        assertEquals(3, result.getSudoRules().size());
        assertTrue(result.getSudoRules().stream().anyMatch(r -> r.contains("(ALL : ALL) ALL")));
        assertTrue(result.getSudoRules().stream().anyMatch(r -> r.contains("/usr/bin/find")));
        assertTrue(result.getSudoRules().stream().anyMatch(r -> r.contains("/opt/backup.py")));

        // Critical findings should include NOPASSWD rules
        List<LinPeasFinding> criticals = result.getCriticalFindings();
        assertFalse(criticals.isEmpty());
        assertTrue(criticals.stream().anyMatch(f -> f.getDetails().contains("NOPASSWD: /usr/bin/find")));
    }

    @Test
    void testParseSuidAndSgidBinaries() {
        String output = """
        ╔══════════╣ SUID - Check easy privesc, exploits and write perms (T1548.001)
        -rwsr-xr-x 1 root root 68208 Jul 14  2021 /usr/bin/passwd
        -rwsr-xr-x 1 root root 88304 Jan 15  2022 /usr/bin/pkexec
        -rwsr-xr-x 1 root root 35000 Mar 10  2021 /usr/bin/find
        -rwsr-xr-x 1 root root 40216 Jan 20  2020 /bin/mount
        
        ╔══════════╣ SGID
        -rwxr-sr-x 1 root shadow 30000 Feb 10 2021 /sbin/unix_chkpwd
        -rwxr-sr-x 1 root mail 15000 Jan 05 2020 /usr/bin/mail
        """;

        LinPeasResult result = parser.parse(output);

        assertTrue(result.getSuidFiles().contains("/usr/bin/passwd"));
        assertTrue(result.getSuidFiles().contains("/usr/bin/pkexec"));
        assertTrue(result.getSuidFiles().contains("/usr/bin/find"));
        assertTrue(result.getSuidFiles().contains("/bin/mount"));

        assertTrue(result.getSgidFiles().contains("/sbin/unix_chkpwd"));
        assertTrue(result.getSgidFiles().contains("/usr/bin/mail"));

        // pkexec and find are high interest SUIDs -> should generate findings
        assertTrue(result.getFindings().stream().anyMatch(f -> f.getTitle().contains("/usr/bin/pkexec")));
        assertTrue(result.getFindings().stream().anyMatch(f -> f.getTitle().contains("/usr/bin/find")));
    }

    @Test
    void testParseLinuxExploitSuggesterAndCves() {
        String output = """
        ╔══════════╣ Linux Exploit Suggester
        [+] [CVE-2021-4034] pwnkit
           Details: https://www.qualys.com/2022/01/25/cve-2021-4034/pwnkit.txt
           Exposure: highly probable
           Tags: ubuntu=10|11|12|13|14|15|16|17|18|19|20|21,debian=7|8|9|10|11
        
        [+] [CVE-2021-3156] sudo Baron Samedit
           Details: https://www.qualys.com/2021/01/26/cve-2021-3156/baron-samedit-heap-based-overflow-in-sudo.txt
           Exposure: probable
        
        [+] [CVE-2016-5195] dirtycow
           Details: https://github.com/dirtycow/dirtycow.github.io/wiki/VulnerabilityDetails
           Exposure: highly probable
        """;

        LinPeasResult result = parser.parse(output);

        assertEquals(3, result.getCves().size());
        assertTrue(result.getCves().stream().anyMatch(c -> "CVE-2021-4034".equalsIgnoreCase(c.getCveId())));
        assertTrue(result.getCves().stream().anyMatch(c -> "CVE-2021-3156".equalsIgnoreCase(c.getCveId())));
        assertTrue(result.getCves().stream().anyMatch(c -> "CVE-2016-5195".equalsIgnoreCase(c.getCveId())));

        assertEquals(3, result.getExploits().size());
        assertTrue(result.getExploits().stream().anyMatch(e -> e.getCodes().contains("CVE-2021-4034")));
        assertTrue(result.getExploits().stream().anyMatch(e -> e.getTitle().contains("pwnkit")));
    }

    @Test
    void testParseListeningPorts() {
        String output = """
        ╔══════════╣ Active Ports & Network Sockets
        tcp        0      0 127.0.0.1:3306          0.0.0.0:*               LISTEN
        tcp        0      0 0.0.0.0:22              0.0.0.0:*               LISTEN
        tcp        0      0 127.0.0.1:8080          0.0.0.0:*               LISTEN
        udp        0      0 0.0.0.0:68              0.0.0.0:*
        """;

        LinPeasResult result = parser.parse(output);

        assertEquals(4, result.getListeningPorts().size());
        assertEquals(4, result.getServicePorts().size());

        ServicePort mysql = result.getServicePorts().stream()
                .filter(p -> p.getPortNumber() == 3306L)
                .findFirst().orElse(null);
        assertNotNull(mysql);
        assertEquals("tcp", mysql.getProtocol());
        assertEquals("127.0.0.1", mysql.getIpAddress());
        assertEquals("LISTEN", mysql.getPortState());
        assertEquals(Provenance.LinPeas.name(), mysql.getProvenance());

        ServicePort ssh = result.getServicePorts().stream()
                .filter(p -> p.getPortNumber() == 22L)
                .findFirst().orElse(null);
        assertNotNull(ssh);
        assertEquals("0.0.0.0", ssh.getIpAddress());
    }

    @Test
    void testParseUsersAndCredentials() {
        String output = """
        ╔══════════╣ Users Information
        root:x:0:0:root:/root:/bin/bash
        daemon:x:1:1:daemon:/usr/sbin:/usr/sbin/nologin
        victim:x:1000:1000:Victim User,,,:/home/victim:/bin/bash
        john:x:1001:1001:John Doe:/home/john:/bin/zsh
        
        ╔══════════╣ Password & Sensitive Files
        root:$6$vQoX1q$8nZbJ7L8R0m3e8vD1g2h3j4k5l6z7x8c9v0b1n2m3q4w5e6r7t8y9u0i:18745:0:99999:7:::
        victim:$y$j9T$a1b2c3d4e5f6g7h8i9j0k1$L8R0m3e8vD1g2h3j4k5l6z7x8c9v0b1n2m3q4w5e6r:18745:0:99999:7:::
        
        ╔══════════╣ SSH Keys
        -----BEGIN RSA PRIVATE KEY-----
        MIIEowIBAAKCAQEA0Y3X2e...
        -----END RSA PRIVATE KEY-----
        """;

        LinPeasResult result = parser.parse(output);

        assertEquals(4, result.getUsers().size());
        assertTrue(result.getUsers().stream().anyMatch(u -> "root".equals(u.getUserName()) && u.getUid() == 0));
        assertTrue(result.getUsers().stream().anyMatch(u -> "victim".equals(u.getUserName()) && "/bin/bash".equals(u.getShell())));

        assertTrue(result.getUsersWithConsole().contains("root"));
        assertTrue(result.getUsersWithConsole().contains("victim"));
        assertTrue(result.getUsersWithConsole().contains("john"));
        assertFalse(result.getUsersWithConsole().contains("daemon"));

        assertEquals(2, result.getPasswordHashes().size());
        assertFalse(result.getSshKeys().isEmpty());
    }

    @Test
    void testParseCronJobs() {
        String output = """
        ╔══════════╣ Cron jobs
        * * * * * root /root/clean_tmp.sh
        0 2 * * * backup /usr/local/bin/backup.sh
        */5 * * * * root /opt/monitor.py
        """;

        LinPeasResult result = parser.parse(output);

        assertEquals(3, result.getCronJobs().size());
        assertTrue(result.getCronJobs().contains("* * * * * root /root/clean_tmp.sh"));
        assertTrue(result.getCronJobs().contains("0 2 * * * backup /usr/local/bin/backup.sh"));
    }

    @Test
    void testAnsiStrippingAndSeverityDetection() {
        // Red on Yellow code: \033[1;31;103m
        String redYellowLine = "\033[1;31;103m[!] Possible sudo token reuse\033[0m";
        // Red code: \033[1;31m
        String redLine = "\033[1;31m[+] /usr/bin/pkexec is SUID\033[0m";
        // Yellow code: \033[1;33m
        String yellowLine = "\033[1;33m[*] Checking files...\033[0m";
        // Plain line
        String plainLine = "Normal informational message";

        assertEquals(LinPeasSeverity.RED_YELLOW, LinPeasParser.detectSeverity(redYellowLine));
        assertEquals(LinPeasSeverity.RED, LinPeasParser.detectSeverity(redLine));
        assertEquals(LinPeasSeverity.YELLOW, LinPeasParser.detectSeverity(yellowLine));
        assertEquals(LinPeasSeverity.INFO, LinPeasParser.detectSeverity(plainLine));

        assertEquals("[!] Possible sudo token reuse", LinPeasParser.stripAnsi(redYellowLine));
        assertEquals("[+] /usr/bin/pkexec is SUID", LinPeasParser.stripAnsi(redLine));
    }

    @Test
    void testContainerAndCloudDetection() {
        String output = """
        ╔══════════╣ Container
        Container related tools present (if any):
        /.dockerenv file found! We are inside a Docker container.
        
        ╔══════════╣ Cloud
        AWS metadata endpoint 169.254.169.254 reachable!
        """;

        LinPeasResult result = parser.parse(output);

        assertEquals("Docker", result.getContainerType());
        assertEquals("AWS", result.getCloudProvider());
    }

    @Test
    void testToScannedTargetsConversion() {
        String output = """
        OS: Linux version 5.15.0-72-generic (buildd@lcy02-amd64-001) (Ubuntu 22.04 LTS)
        Hostname: target-srv
        
        ╔══════════╣ Linux Exploit Suggester
        [+] [CVE-2022-0847] Dirty Pipe
        
        ╔══════════╣ Active Ports
        tcp 0 0 0.0.0.0:80 0.0.0.0:* LISTEN
        
        ╔══════════╣ Users
        victim:x:1000:1000:Victim User:/home/victim:/bin/bash
        """;

        LinPeasResult result = parser.parse(output);

        Target parentTarget = new Target();
        parentTarget.setId(42L);
        parentTarget.setTargetType(TargetType.Ipv4.name());
        parentTarget.setLevel(1);

        ScannedTargets st = result.toScannedTargets(parentTarget);
        assertNotNull(st);

        // OS Target
        assertFalse(st.getTargets().isEmpty());
        Target osTarget = st.getTargets().iterator().next();
        assertEquals(TargetType.OperatingSystem.name(), osTarget.getTargetType());
        assertEquals(42L, osTarget.getParentId());
        assertEquals(2, osTarget.getLevel());
        assertEquals("Ubuntu", osTarget.getName());
        assertEquals("target-srv", osTarget.getUri());

        // CVE
        assertFalse(st.getCves().isEmpty());
        CVE cve = st.getCves().iterator().next();
        assertEquals("CVE-2022-0847", cve.getCveId());
        assertEquals(42L, cve.getParentId());
        assertEquals(2, cve.getLevel());

        // Exploit
        assertFalse(st.getExploits().isEmpty());
        Exploit exp = st.getExploits().iterator().next();
        assertEquals("CVE-2022-0847", exp.getCodes());
        assertEquals(42L, exp.getTargetId());

        // ServicePort
        assertFalse(st.getServicePorts().isEmpty());
        ServicePort sp = st.getServicePorts().iterator().next();
        assertEquals(80L, sp.getPortNumber());
        assertEquals(42L, sp.getParentId());

        // User
        assertFalse(st.getUsers().isEmpty());
        ScannedUser user = st.getUsers().iterator().next();
        assertEquals("victim", user.getUserName());
        assertEquals(42L, user.getParentId());
    }

    @Test
    void testContextIntegrationAndFileParsing() throws IOException {
        File tempFile = File.createTempFile("linpeas_test", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("""
            OS: Linux version 5.4.0-42-generic #46-Ubuntu
            Hostname: vm-host
            User & Groups: uid=1000(user) gid=1000(user) groups=1000(user),27(sudo)
            """);
        }

        Context ctx = new Context();
        LinPeasResult result = parser.parse(ctx, tempFile.getAbsolutePath());

        assertNotNull(result);
        assertEquals("vm-host", result.getHostname());
        assertEquals("user", result.getCurrentUser());

        LinPeasResult stashed = ctx.fetch(Tag.LinPeas);
        assertNotNull(stashed);
        assertEquals("vm-host", stashed.getHostname());
    }

    @Test
    void testEmptyAndNullInputs() {
        LinPeasResult r1 = parser.parse((String) null);
        assertNotNull(r1);
        assertTrue(r1.getSections().isEmpty());

        LinPeasResult r2 = parser.parse("");
        assertNotNull(r2);
        assertTrue(r2.getSections().isEmpty());

        LinPeasResult r3 = parser.parse(List.of());
        assertNotNull(r3);
        assertTrue(r3.getSections().isEmpty());

        LinPeasResult r4 = parser.parseFile((File) null);
        assertNotNull(r4);

        LinPeasResult r5 = parser.parseFile("non_existent_file_12345.txt");
        assertNotNull(r5);
    }
}
