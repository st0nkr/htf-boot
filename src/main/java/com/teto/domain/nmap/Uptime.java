package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class Uptime extends BaseEntity {
    private final Long seconds;
    private final String lastBoot;

    public Uptime(Long seconds, String lastBoot) {
        this.seconds = seconds;
        this.lastBoot = lastBoot;
    }

    public Long getSeconds() {
        return seconds;
    }

    public String getLastBoot() {
        return lastBoot;
    }
}
