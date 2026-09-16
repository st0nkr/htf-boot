package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

public class PortUsed extends BaseEntity {
    private final String state;
    private final String proto;
    private final Long portId;

    public PortUsed(String state, String proto, Long portId) {
        this.state = state;
        this.proto = proto;
        this.portId = portId;
    }

    public String getState() {
        return state;
    }

    public String getProto() {
        return proto;
    }

    public Long getPortId() {
        return portId;
    }
}
