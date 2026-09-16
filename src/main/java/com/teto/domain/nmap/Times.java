package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class Times extends BaseEntity {
    private final Long srtt;
    private final Long rttvar;
    private final Long to;

    public Times(Long srtt, Long rttvar, Long aLong) {
        this.srtt = srtt;
        this.rttvar = rttvar;
        to = aLong;
    }

    public Long getSrtt() {
        return srtt;
    }

    public Long getRttvar() {
        return rttvar;
    }

    public Long getTo() {
        return to;
    }
}
