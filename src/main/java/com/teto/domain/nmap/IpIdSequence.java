package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

import java.util.List;

public class IpIdSequence extends BaseEntity {
    private final String clazz;
    private final List<Integer> values;

    public IpIdSequence(String clazz, List<Integer> values) {
        this.clazz = clazz;
        this.values = values;
    }

    public String getClazz() {
        return clazz;
    }

    public List<Integer> getValues() {
        return values;
    }
}
