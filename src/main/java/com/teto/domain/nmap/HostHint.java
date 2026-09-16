package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

import java.util.List;

public class HostHint extends BaseEntity {
    private final Status status;
    private final List<Address> addresses;
    private final HostNames hostNames;

    public HostHint(Status status, List<Address> addresses, HostNames hostNames) {
        this.status = status;
        this.addresses = addresses;
        this.hostNames = hostNames;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Status getStatus() {
        return status;
    }


    public HostNames getHostNames() {
        return hostNames;
    }
}