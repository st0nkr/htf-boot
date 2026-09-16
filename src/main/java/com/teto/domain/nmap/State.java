package com.teto.domain.nmap;

public class State  {
    private final String state;
    private final String reason;
    private final String reasonTtl;

    public State(String state, String reason, String reasonTtl) {
        this.state = state;
        this.reason = reason;
        this.reasonTtl = reasonTtl;
    }

    public String getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    public String getReasonTtl() {
        return reasonTtl;
    }

    @Override
    public String toString() {
        return "State{" +
                "state='" + state + '\'' +
                '}';
    }
}
