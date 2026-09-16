package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class HostHint extends BaseEntity {
    private final Status status;
    private final Address address;
    private final HostNames hostNames;

    public HostHint(Status status, Address address, HostNames hostNames) {
        this.status = status;
        this.address = address;
        this.hostNames = hostNames;
    }

    public Status getStatus() {
        return status;
    }

    public Address getAddress() {
        return address;
    }

    public HostNames getHostNames() {
        return hostNames;
    }
}