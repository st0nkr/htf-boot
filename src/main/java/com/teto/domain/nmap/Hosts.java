package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class Hosts extends BaseEntity {
    private final Long up;
    private final Long down;
    private final Long total;

    public Hosts(Long up, Long down, Long total) {
        this.up = up;
        this.down = down;
        this.total = total;
    }

    public Long getUp() {
        return up;
    }

    public Long getDown() {
        return down;
    }

    public Long getTotal() {
        return total;
    }
}
