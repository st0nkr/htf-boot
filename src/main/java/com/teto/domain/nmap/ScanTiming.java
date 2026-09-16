package com.teto.domain.nmap;

public enum ScanTiming {
    paranoid("-T0"),
    sneaky("-T1"),
    polite("-T2"),
    normal("-T3"),
    aggressive("-T4"),
    insane("-T5");

    private String flag;
    ScanTiming(String flag) {
        this.flag = flag;
    }

    public static ScanTiming fromInt(Integer scanTiming) {
        String flg = "-T"+scanTiming;
        for(var st : values()) {
            if(st.flag.equals(flg)) {
                return st;
            }
        }
        return null;
    }

    public String getFlag() {
        return flag;
    }
    public static ScanTiming fromString(String name) {
        for(ScanTiming st : values()) {
            if(st.name().equals(name) || st.flag.equals(name)) {
                return st;
            }
        }
        return null;
    }
}
