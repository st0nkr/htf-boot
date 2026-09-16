package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class HostName extends BaseEntity {
    private final String name;
    private final String type;

    public HostName(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}