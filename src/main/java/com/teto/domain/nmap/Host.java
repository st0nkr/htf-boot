package com.teto.domain.nmap;

import java.util.List;

public class Host {
    private final String startTime;
    private final String endTime;

    private final Status status;
    private final List<Address> addresses;
    private final HostNames hostNames;
    private final Trace trace;
    private final Ports ports;
    private final OS os;
    private final Uptime uptime;
    private final TcpSequence tcpSequence;
    private final IpIdSequence ipIdSequence;
    private final TcpTsSequence tcpTsSequence;
    private final Times times;
    private final HostScript hostScript;

    public Host(String startTime, String endTime, Status status, List<Address> addresses, HostNames hostNames, Ports ports, HostScript hs, OS os,
                Uptime uptime, TcpSequence tcpSequence, IpIdSequence ipIdSequence, TcpTsSequence tcpTsSequence,
                Times times, Trace trace) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.addresses = addresses;
        this.hostNames = hostNames;
        this.ports = ports;
        this.hostScript = hs;
        this.os = os;
        this.uptime = uptime;
        this.tcpSequence = tcpSequence;
        this.ipIdSequence = ipIdSequence;
        this.tcpTsSequence = tcpTsSequence;
        this.times = times;
        this.trace = trace;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public HostScript getHostScript() {
        return hostScript;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Status getStatus() {
        return status;
    }

    public HostNames getHostNames() {
        return hostNames;
    }

    public Trace getTrace() {
        return trace;
    }

    public Ports getPorts() {
        return ports;
    }

    public OS getOs() {
        return os;
    }

    public Uptime getUptime() {
        return uptime;
    }

    public TcpSequence getTcpSequence() {
        return tcpSequence;
    }

    public IpIdSequence getIpIdSequence() {
        return ipIdSequence;
    }

    public TcpTsSequence getTcpTsSequence() {
        return tcpTsSequence;
    }

    public Times getTimes() {
        return times;
    }
}
