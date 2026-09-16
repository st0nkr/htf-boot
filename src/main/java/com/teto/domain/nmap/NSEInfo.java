package com.teto.domain.nmap;

import com.teto.domain.target.TargetType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class NSEInfo {
    private Map<TargetType, Collection<String>> targets;

    private List<HostVersionOS> hostVersionOs = new ArrayList<>();

    public List<HostVersionOS> getHostVersionOs() {
        return hostVersionOs;
    }

    public void setHostVersionOs(List<HostVersionOS> hostVersionOs) {
        this.hostVersionOs = hostVersionOs;
    }

    public Map<TargetType, Collection<String>> getTargets() {
        return targets;
    }

    public void setTargets(Map<TargetType, Collection<String>> targets) {
        this.targets = targets;
    }
}
