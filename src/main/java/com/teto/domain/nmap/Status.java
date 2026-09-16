package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class Status extends BaseEntity {
    private final String state;
    private final String reason;
    private final String reasonTtl;

    public Status(String state, String reason, String reasonTtl) {
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
}