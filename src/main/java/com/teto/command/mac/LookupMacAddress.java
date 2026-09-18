package com.teto.command.mac;

import com.teto.IFile;
import com.teto.IJSoup;
import com.teto.IMAC;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.mac.MacDetails;
import com.teto.domain.target.Target;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LookupMacAddress extends AbstractCommand<MacDetails> implements IMAC, IFile, IJSoup {
    private static final Pattern MAC_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$|^([0-9A-Fa-f]{4}\\.[0-9A-Fa-f]{4}\\.[0-9A-Fa-f]{4})$|^([0-9A-Fa-f]{12})$|^([0-9A-Fa-f]{6})$");
    private static final Pattern CLEAN_HEX_PATTERN = Pattern.compile("[^0-9A-Fa-f]");

    private static final String[] PREFIX_FILES = {
            "/usr/share/nmap/nmap-mac-prefixes",
            "/var/lib/ieee-data/oui.txt",
            "/usr/share/ieee-data/oui.txt",
            "/var/lib/ieee-data/oui.csv",
            "/usr/share/ieee-data/oui.csv",
            "/usr/share/wireshark/manuf"
    };

    private static final Map<String, String> LOCAL_CACHE = new ConcurrentHashMap<>();
    private static volatile boolean localDbLoaded = false;
    private static final Object LOAD_LOCK = new Object();

    // Built-in common vendor prefixes as fallback
    private static final Map<String, String> BUILT_IN_OUIS = new HashMap<>();

    static {
        BUILT_IN_OUIS.put("005056", "VMware, Inc.");
        BUILT_IN_OUIS.put("000C29", "VMware, Inc.");
        BUILT_IN_OUIS.put("000569", "VMware, Inc.");
        BUILT_IN_OUIS.put("001C14", "VMware, Inc.");
        BUILT_IN_OUIS.put("080027", "Oracle VirtualBox");
        BUILT_IN_OUIS.put("525400", "QEMU / KVM Virtual NIC");
        BUILT_IN_OUIS.put("00155D", "Microsoft Corporation (Hyper-V)");
        BUILT_IN_OUIS.put("00000C", "Cisco Systems, Inc.");
        BUILT_IN_OUIS.put("000142", "Cisco Systems, Inc.");
        BUILT_IN_OUIS.put("000143", "Cisco Systems, Inc.");
        BUILT_IN_OUIS.put("000163", "Cisco Systems, Inc.");
        BUILT_IN_OUIS.put("00036B", "Cisco Systems, Inc.");
        BUILT_IN_OUIS.put("0003E3", "Cisco Systems, Inc.");
        BUILT_IN_OUIS.put("000000", "Xerox Corporation");
        BUILT_IN_OUIS.put("000393", "Apple, Inc.");
        BUILT_IN_OUIS.put("000502", "Apple, Inc.");
        BUILT_IN_OUIS.put("000A27", "Apple, Inc.");
        BUILT_IN_OUIS.put("000A95", "Apple, Inc.");
        BUILT_IN_OUIS.put("0017F2", "Apple, Inc.");
        BUILT_IN_OUIS.put("0019E3", "Apple, Inc.");
        BUILT_IN_OUIS.put("ACDE48", "Apple, Inc.");
        BUILT_IN_OUIS.put("F01898", "Apple, Inc.");
        BUILT_IN_OUIS.put("B827EB", "Raspberry Pi Foundation");
        BUILT_IN_OUIS.put("DC2632", "Raspberry Pi Foundation");
        BUILT_IN_OUIS.put("E45F01", "Raspberry Pi Trading Ltd");
        BUILT_IN_OUIS.put("28CDC4", "Raspberry Pi Trading Ltd");
        BUILT_IN_OUIS.put("001A11", "Google, Inc.");
        BUILT_IN_OUIS.put("3C5AB4", "Google, Inc.");
        BUILT_IN_OUIS.put("546009", "Google, Inc.");
        BUILT_IN_OUIS.put("D83ADD", "Google, Inc.");
        BUILT_IN_OUIS.put("001422", "Dell Inc.");
        BUILT_IN_OUIS.put("00188B", "Dell Inc.");
        BUILT_IN_OUIS.put("1866DA", "Dell Inc.");
        BUILT_IN_OUIS.put("001E68", "HP Inc.");
        BUILT_IN_OUIS.put("00215A", "HP Inc.");
        BUILT_IN_OUIS.put("000E7F", "Hewlett Packard Enterprise");
        BUILT_IN_OUIS.put("001B78", "Hewlett Packard Enterprise");
        BUILT_IN_OUIS.put("001B21", "Intel Corporate");
        BUILT_IN_OUIS.put("001302", "Intel Corporate");
        BUILT_IN_OUIS.put("A44CC8", "Intel Corporate");
        BUILT_IN_OUIS.put("001E10", "Huawei Technologies Co., Ltd");
        BUILT_IN_OUIS.put("00259E", "Huawei Technologies Co., Ltd");
        BUILT_IN_OUIS.put("0012FB", "Samsung Electronics Co., Ltd");
        BUILT_IN_OUIS.put("00166C", "Samsung Electronics Co., Ltd");
        BUILT_IN_OUIS.put("0019E0", "TP-Link Corporation Limited");
        BUILT_IN_OUIS.put("50C7BF", "TP-Link Corporation Limited");
    }

    private final String macAddress;
    private final Target target;
    private boolean onlineFallback = true;

    public LookupMacAddress(String macAddress) {
        this.macAddress = macAddress;
        this.target = null;
    }

    public LookupMacAddress(String macAddress, boolean onlineFallback) {
        this.macAddress = macAddress;
        this.target = null;
        this.onlineFallback = onlineFallback;
    }

    public LookupMacAddress(Target target) {
        this.target = target;
        this.macAddress = target != null ? target.getMacAddress() : null;
    }

    public LookupMacAddress(Target target, boolean onlineFallback) {
        this.target = target;
        this.macAddress = target != null ? target.getMacAddress() : null;
        this.onlineFallback = onlineFallback;
    }

    public static Optional<MacDetails> lookup(String macAddress) {
        return new LookupMacAddress(macAddress).resolve();
    }

    public static Optional<String> lookupManufacturer(String macAddress) {
        return lookup(macAddress).map(MacDetails::getManufacturer);
    }

    public static Optional<MacDetails> lookup(Target target) {
        if (target == null || target.getMacAddress() == null) {
            return Optional.empty();
        }
        Optional<MacDetails> details = new LookupMacAddress(target).resolve();
        details.ifPresent(d -> {
            if (d.getManufacturer() != null) {
                target.setManufacturer(d.getManufacturer());
                if (target.getVendor() == null || target.getVendor().isEmpty()) {
                    target.setVendor(d.getManufacturer());
                }
            }
        });
        return details;
    }

    @Override
    public Optional<MacDetails> apply(Context ctx) {
        Optional<MacDetails> result = resolve();
        if (result.isPresent() && target != null) {
            MacDetails details = result.get();
            if (details.getManufacturer() != null) {
                target.setManufacturer(details.getManufacturer());
                if (target.getVendor() == null || target.getVendor().isEmpty()) {
                    target.setVendor(details.getManufacturer());
                }
            }
        }
        return result;
    }

    public Optional<MacDetails> resolve() {
        if (macAddress == null || macAddress.trim().isEmpty()) {
            return Optional.empty();
        }

        String rawMac = macAddress.trim();
        String hex = CLEAN_HEX_PATTERN.matcher(rawMac).replaceAll("").toUpperCase();

        if (hex.length() < 6) {
            return Optional.empty();
        }

        String oui = hex.substring(0, 6);
        String formattedMac = formatMacAddress(hex);
        String formattedOui = String.format("%s:%s:%s", oui.substring(0, 2), oui.substring(2, 4), oui.substring(4, 6));

        MacDetails details = new MacDetails();
        details.setMacAddress(rawMac);
        details.setNormalizedMac(formattedMac);
        details.setOui(formattedOui);
        details.setValid(isValidMac(rawMac, hex));

        // Evaluate address characteristics based on first byte
        try {
            int firstByte = Integer.parseInt(oui.substring(0, 2), 16);
            boolean multicast = (firstByte & 1) == 1;
            boolean locallyAdministered = (firstByte & 2) == 2;

            details.setMulticast(multicast);
            details.setUnicast(!multicast);
            details.setLocallyAdministered(locallyAdministered);
            details.setUniversallyAdministered(!locallyAdministered);
        } catch (Exception ignored) {
        }

        // 1. Try local database files (nmap, wireshark, ieee)
        String vendor = lookupLocalDatabase(oui);
        if (vendor != null && !vendor.isEmpty()) {
            details.setManufacturer(vendor);
            details.setSource("local-database");
            return Optional.of(details);
        }

        // 2. Try built-in OUI definitions
        if (BUILT_IN_OUIS.containsKey(oui)) {
            details.setManufacturer(BUILT_IN_OUIS.get(oui));
            details.setSource("built-in-oui");
            return Optional.of(details);
        }

        // 3. Optional Online API Lookup fallback
        if (onlineFallback) {
            String onlineVendor = lookupOnlineApi(formattedMac != null ? formattedMac : rawMac);
            if (onlineVendor != null && !onlineVendor.isEmpty()) {
                details.setManufacturer(onlineVendor);
                details.setSource("online-api");
                LOCAL_CACHE.put(oui, onlineVendor);
                return Optional.of(details);
            }
        }

        return Optional.of(details);
    }

    private boolean isValidMac(String raw, String hex) {
        if (hex.length() == 12 || hex.length() == 6) {
            return true;
        }
        return MAC_PATTERN.matcher(raw).matches();
    }

    public static String formatMacAddress(String hex) {
        if (hex == null || hex.length() < 6) {
            return hex;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            if (i > 0) sb.append(":");
            if (i + 2 <= hex.length()) {
                sb.append(hex, i, i + 2);
            } else {
                sb.append(hex.substring(i));
            }
        }
        return sb.toString();
    }

    private String lookupLocalDatabase(String oui) {
        if (LOCAL_CACHE.containsKey(oui)) {
            return LOCAL_CACHE.get(oui);
        }

        ensureLocalDatabaseLoaded();

        return LOCAL_CACHE.get(oui);
    }

    private void ensureLocalDatabaseLoaded() {
        if (!localDbLoaded) {
            synchronized (LOAD_LOCK) {
                if (!localDbLoaded) {
                    loadLocalDatabases();
                    localDbLoaded = true;
                }
            }
        }
    }

    private void loadLocalDatabases() {
        for (String filePath : PREFIX_FILES) {
            File file = new File(filePath);
            if (file.exists() && file.canRead()) {
                loadPrefixFile(file);
            }
        }
    }

    private void loadPrefixFile(File file) {
        String fileName = file.getName().toLowerCase();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (fileName.endsWith(".csv")) {
                    parseCsvLine(line);
                } else if (fileName.contains("nmap")) {
                    parseNmapLine(line);
                } else if (fileName.contains("manuf")) {
                    parseManufLine(line);
                } else if (fileName.contains("oui")) {
                    parseOuiTxtLine(line);
                }
            }
        } catch (Exception e) {
            warn(this, "Failed to load prefix file: " + file.getAbsolutePath());
        }
    }

    private void parseNmapLine(String line) {
        // Format: 00000C Cisco Systems
        int spaceIdx = line.indexOf(' ');
        if (spaceIdx > 0) {
            String prefix = line.substring(0, spaceIdx).trim().toUpperCase();
            String vendor = line.substring(spaceIdx + 1).trim();
            if (prefix.length() == 6 && !vendor.isEmpty()) {
                LOCAL_CACHE.putIfAbsent(prefix, vendor);
            }
        }
    }

    private void parseManufLine(String line) {
        // Format: 00:00:0C Cisco Cisco Systems
        String[] parts = line.split("\\s+", 3);
        if (parts.length >= 2) {
            String prefix = CLEAN_HEX_PATTERN.matcher(parts[0]).replaceAll("").toUpperCase();
            if (prefix.length() >= 6) {
                String oui = prefix.substring(0, 6);
                String vendor = parts.length >= 3 ? parts[2] : parts[1];
                LOCAL_CACHE.putIfAbsent(oui, vendor);
            }
        }
    }

    private void parseCsvLine(String line) {
        // Format: Registry,Assignment,Organization Name,Organization Address
        String[] parts = line.split(",", 4);
        if (parts.length >= 3) {
            String assignment = CLEAN_HEX_PATTERN.matcher(parts[1].replace("\"", "")).replaceAll("").toUpperCase();
            String orgName = parts[2].replace("\"", "").trim();
            if (assignment.length() >= 6 && !orgName.isEmpty()) {
                LOCAL_CACHE.putIfAbsent(assignment.substring(0, 6), orgName);
            }
        }
    }

    private void parseOuiTxtLine(String line) {
        // Format: 00-00-0C   (hex)   Cisco Systems, Inc.
        if (line.contains("(hex)")) {
            String[] parts = line.split("\\(hex\\)");
            if (parts.length >= 2) {
                String prefix = CLEAN_HEX_PATTERN.matcher(parts[0]).replaceAll("").toUpperCase();
                String vendor = parts[1].trim();
                if (prefix.length() >= 6 && !vendor.isEmpty()) {
                    LOCAL_CACHE.putIfAbsent(prefix.substring(0, 6), vendor);
                }
            }
        }
    }

    private String lookupOnlineApi(String mac) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();

            // Try api.macvendors.com
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.macvendors.com/" + mac))
                    .timeout(Duration.ofSeconds(4))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && response.body() != null && !response.body().trim().isEmpty()) {
                String body = response.body().trim();
                if (!body.contains("errors") && !body.contains("Not Found") && !body.contains("vendor not found")) {
                    return body;
                }
            }
        } catch (Exception ignored) {
        }

        try {
            // Try maclookup.app fallback
            String clean = CLEAN_HEX_PATTERN.matcher(mac).replaceAll("").toUpperCase();
            if (clean.length() >= 6) {
                String oui = clean.substring(0, 6);
                Document doc = Jsoup.connect("https://api.maclookup.app/v2/macs/" + oui)
                        .timeout(4000)
                        .ignoreContentType(true)
                        .get();
                if (doc != null && doc.body() != null) {
                    String json = doc.body().text();
                    int companyIdx = json.indexOf("\"company\":");
                    if (companyIdx != -1) {
                        int start = json.indexOf('"', companyIdx + 10);
                        int end = json.indexOf('"', start + 1);
                        if (start != -1 && end != -1) {
                            String company = json.substring(start + 1, end).trim();
                            if (!company.isEmpty()) {
                                return company;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return null;
    }
}
