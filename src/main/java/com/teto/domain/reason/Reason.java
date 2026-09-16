package com.teto.domain.reason;

public enum Reason {
    SynAck("syn-ack"),
    Reset("reset"),
    NoResponse("no-response");
    private String str;

    Reason() {

    }
    Reason(String str) {
        this.str = str;
    }

    public static Reason fromString(String r) {
        for(Reason reason : Reason.values()) {
            if(reason.name().equals(r)) {
                return reason;
            }
            if(reason.str.equals(r)) {
                return reason;
            }
        }
        return null;
    }
}
