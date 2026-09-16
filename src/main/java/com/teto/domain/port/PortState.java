package com.teto.domain.port;

public enum PortState {
    blocked, open,closed;

    public static PortState fromString(String state) {
        for(PortState ps : PortState.values()) {
            if(ps.name().equals(state)) {
                return ps;
            }
        }
        return null;
    }
}
