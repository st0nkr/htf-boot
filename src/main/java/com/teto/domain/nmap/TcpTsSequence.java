package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class TcpTsSequence extends BaseEntity {
    private final String clazz;
    private final String values;

    public TcpTsSequence(String clazz, String values) {
        this.clazz = clazz;
        this.values = values;
    }

    public String getClazz() {
        return clazz;
    }

    public String getValues() {
        return values;
    }
}
