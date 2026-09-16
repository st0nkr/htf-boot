package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

import java.util.List;

public class OS extends BaseEntity {
    private final List<PortUsed> portsUsed;
    private final List<OSMatch> osMatches;
    private final List<FingerPrint> fingerPrints;

    public OS(List<PortUsed> portsUsed, List<OSMatch> osMatches, List<FingerPrint> fps) {
        this.portsUsed = portsUsed;
        this.osMatches = osMatches;
        this.fingerPrints = fps;
    }

    public List<PortUsed> getPortsUsed() {
        return portsUsed;
    }

    public List<OSMatch> getOsMatches() {
        return osMatches;
    }

    public List<FingerPrint> getFingerPrints() {
        return fingerPrints;
    }
}
