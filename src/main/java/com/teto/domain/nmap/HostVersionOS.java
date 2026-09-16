package com.teto.domain.nmap;

public class HostVersionOS {
    private String host;
    private String version;
    private String os;

    public HostVersionOS() {}
    public HostVersionOS(String h, String v, String o) {
        this.host = h;
        this.version = v;
        this.os = o;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
