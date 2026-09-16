package com.teto.domain.cve;

public enum CVEType {
    VULDB("VulDB"),
    MITRE("MITRE CVE"),
    SECURIYFOCUS("SecurityFocus"),
    IBM_X_FORCE("IBM X-Force"),
    EXPLOIT_DB("Exploit-DB"),
    OPENVAS("OpenVAS"),
    SecurityTracker("SecurityTracker"),
    OSVDB("OSVDB");

    private String tag;

    public String getTag() {
        return tag;
    }
    CVEType(String tag) {
        this.tag = tag;
    }

    public static CVEType fromString(String str) {
        for(CVEType ct : values()) {
            if(ct.name().equalsIgnoreCase(str)) {
                return ct;
            }
            if(ct.tag.equalsIgnoreCase(str)) {
                return ct;
            }
        }
        return null;
    }
}
