package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

import java.util.List;
public class HostNames extends BaseEntity {
    private final List<HostName> hostNames;

    public HostNames(List<HostName> hostNames) {
        this.hostNames = hostNames;
    }

    public List<HostName> getHostNames() {
        return hostNames;
    }
}
