package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class ExtraReasons extends BaseEntity {
    private final String reason;
    private final Long count;
    private final String proto;
    private final String ports;

    public ExtraReasons(String reason, Long count, String proto, String ports) {
        this.reason = reason;
        this.count = count;
        this.proto = proto;
        this.ports = ports;
    }

    public String getReason() {
        return reason;
    }

    public Long getCount() {
        return count;
    }

    public String getProto() {
        return proto;
    }

    public String getPorts() {
        return ports;
    }
}
