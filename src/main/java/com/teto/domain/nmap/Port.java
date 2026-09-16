package com.teto.domain.nmap;


import java.util.List;

public class Port {
    private final String protocol;
    private final Long portId;
    private final State state;
    private final Service service;
    private final List<NSEScript> nseScripts;
    public Port(String protocol, Long portId, State state, Service service, List<NSEScript> nseScripts) {
        this.protocol = protocol;
        this.portId = portId;
        this.state = state;
        this.service = service;
        this.nseScripts = nseScripts;
        if(service != null) {
            this.service.setState(state);
            this.service.setPortId(portId);
            this.service.setProtocol(protocol);
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public Long getPortId() {
        return portId;
    }

    public State getState() {
        return state;
    }

    public Service getService() {
        return service;
    }

    public List<NSEScript> getNseScripts() {
        return nseScripts;
    }

    @Override
    public String toString() {
        return "Port{" +
                "protocol='" + protocol + '\'' +
                ", portId=" + portId +
                ", state=" + state.toString() +
                ", service=" + service +
                '}';
    }
}
