package com.teto.domain.script;

public enum ScriptCategory {
    // $Tag
    NMAP,
    ARP,
    LDAP,
    NamedService,
    BannerGrab,
    WAF,
    SNMP,
    FTP,
    CURL,
    NETBIOS,
    CVS,
    OpenEye,
    VNC,
    Redis,
    PSQL,
    WindowsRM,
    MYSQL,
    MSSQL,
    MQTT,
    MongoDB,
    Cassandra,
    RDP,
    Http,
    Https,
    Telnet,
    WordPress,
    Joomla,
    Dicom,
    POP3,
    IMAP,
    WORDLIST,
    NFS,
    Crawl,
    DNS,
    EMAIL,
    DOMAIN,
    ENUM,
    SUBDOMAIN,
    HPING3,
    VULNERABILITY,
    TECHNOLOGY,
    DIRECTORIES,
    USERNAMES,
    SQL,
    ASN,
    SIP,
    SSH,
    BANNERS,
    SAMBA;

    public static ScriptCategory fromString(String str) {
        for(ScriptCategory sc : values()) {
            if(sc.name().equalsIgnoreCase(str)) {
                return sc;
            }
        }
        return null;
    }
}
