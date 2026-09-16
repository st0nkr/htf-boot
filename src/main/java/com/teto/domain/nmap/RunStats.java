package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class RunStats extends BaseEntity {
    private final Finished finished;
    private final Hosts hosts;

    public RunStats(Finished finished, Hosts hosts) {
        this.finished = finished;
        this.hosts = hosts;
    }

    public Finished getFinished() {
        return finished;
    }

    public Hosts getHosts() {
        return hosts;
    }
}
