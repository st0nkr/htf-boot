package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class Elem extends BaseEntity {
    private final String key;
    private final String value;

    public Elem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
