package com.teto.domain.nmap;

public class Hop {
    private String host;
    private String ipAddr;
    private Double ttl;
    private Integer rtt;

    public Hop(String host, String ipAddr, Double ttl, Integer rtt) {
        this.host = host;
        this.ipAddr = ipAddr;
        this.rtt = rtt;
        this.ttl = ttl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Double getTtl() {
        return ttl;
    }

    public void setTtl(Double ttl) {
        this.ttl = ttl;
    }

    public Integer getRtt() {
        return rtt;
    }

    public void setRtt(Integer rtt) {
        this.rtt = rtt;
    }
}
