package com.teto.domain.nmap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class NmapRun  {
    private final String scanner;
    private final String args;
    private final Long start;
    private final String startstr;
    private final String version;
    private final String xmlOutputVersion;
    @JsonIgnore
    private final ScanInfo scanInfo[];
    @JsonIgnore
    private final Verbose verbose;
    @JsonIgnore
    private final Debugging debugging;
    private final List<HostHint> hostHints;
    private final List<Host> hosts;
    private final RunStats runStats;

    public NmapRun(String scanner, String args, Long start, String startstr, String version, String xmlOutputVersion, ScanInfo[] scanInfos, Verbose verbose, Debugging debugging, List<HostHint> hostHints, List<Host> hosts, RunStats runStats) {
        this.scanner = scanner;
        this.args = args;
        this.start = start;
        this.startstr = startstr;
        this.version = version;
        this.xmlOutputVersion = xmlOutputVersion;
        this.scanInfo = scanInfos;
        this.verbose = verbose;
        this.debugging = debugging;
        this.hostHints = hostHints;
        this.hosts = hosts;
        this.runStats = runStats;
    }

    public String getScanner() {
        return scanner;
    }

    public String getArgs() {
        return args;
    }

    public Long getStart() {
        return start;
    }

    public String getStartstr() {
        return startstr;
    }

    public String getVersion() {
        return version;
    }

    public String getXmlOutputVersion() {
        return xmlOutputVersion;
    }

    public ScanInfo[] getScanInfo() {
        return scanInfo;
    }

    public Verbose getVerbose() {
        return verbose;
    }

    public Debugging getDebugging() {
        return debugging;
    }

    public List<HostHint> getHostHints() {
        return hostHints;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public RunStats getRunStats() {
        return runStats;
    }

    public Integer getMTU() {
        List<Host> hsts = getHosts();
        if(hsts == null || hsts.size() != 1) {
            return null;
        }
        Host hst = hsts.get(0);
        if(hst == null) {
            return null;
        }
        HostScript hs = hst.getHostScript();
        if(hs == null) {
            return null;
        }
        if(hs.getScripts() == null) {
            return null;
        }
        for(Script script : hs.getScripts()) {
            if(script.getMtu() != null) {
                return script.getMtu();
            }
        }
        return null;
    }

    public List<String> getHops() {
        List<Host> hsts = getHosts();
        if(hsts == null || hsts.size() != 1) {
            return null;
        }
        Host hst = hsts.get(0);
        if(hst == null) {
            return null;
        }
        Trace trace = hst.getTrace();
        if(trace == null) {
            return null;
        }
        List<Hop> hops = trace.getHops();
        if(hops == null) {
            return null;
        }
        final List<String> addrs = new ArrayList<>();
        for(Hop hop : hops) {
            if(hop != null && hop.getIpAddr() != null) {
                addrs.add(hop.getIpAddr());
            }
        }
        return addrs;
    }
}