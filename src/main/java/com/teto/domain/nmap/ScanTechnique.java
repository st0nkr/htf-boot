package com.teto.domain.nmap;

public enum ScanTechnique {
    SYN("-sS"),
    SYN_BADSUM("-sS --badsum"),
    Connect("-sT"),
    ACK("-sA"),
    Window("-sW"),
    Maimon("-sM"),
    Null("-sN"),
    FIN("-sF"),
    Xmas("-sX"),
    Protocol("-sO"),
    SCTP("-sY"),
    CookieEcho("-sZ"),
    //Zombie("-sI"),
    Ping("-sn"),
    UDP("-sU"),
    IPProtocol("-sO"),
    FTPBounce("-b"),
    Zombie("-sI"),
    ServiceVersion("-sV"),
    List("-sL");
    private String flag;
    ScanTechnique(String flg) {
        this.flag = flg;
    }

    public String getFlag() {
        return flag;
    }
    public static ScanTechnique fromString(String name) {
        for(ScanTechnique st : values()) {
            if(st.name().equals(name) || st.flag.equals(name)) {
                return st;
            }
        }
        return null;
    }
}
