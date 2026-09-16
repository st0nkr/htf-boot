package com.teto.domain.nmap;


import java.util.List;

public class Trace {
    private final String port;
    private final String proto;
    private final List<Hop> hops;

    public Trace(String port, String proto, List<Hop> hops) {
        this.port = port;
        this.proto = proto;
        this.hops = hops;
    }

    public String getPort() {
        return port;
    }

    public String getProto() {
        return proto;
    }

    public List<Hop> getHops() {
        return hops;
    }
}
