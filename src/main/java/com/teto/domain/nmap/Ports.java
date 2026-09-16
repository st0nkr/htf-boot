package com.teto.domain.nmap;


import java.util.List;
public class Ports  {
    private final List<ExtraPorts> extraPorts;
    private final List<Port> ports;
    public Ports(List<ExtraPorts> extraPorts, List<Port> ports) {
        this.extraPorts = extraPorts;
        this.ports = ports;
    }

    public List<ExtraPorts> getExtraPorts() {
        return extraPorts;
    }

    public List<Port> getPorts() {
        return ports;
    }
}
