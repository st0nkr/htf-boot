package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

import java.util.List;

public class OSMatch extends BaseEntity {
    private final String name;
    private final Long accuracy;
    private final Long line;
    private final List<OSClass> osClasses;

    public OSMatch(String name, Long accuracy, Long line, List<OSClass> osClasses) {
        this.name = name;
        this.accuracy = accuracy;
        this.line = line;
        this.osClasses = osClasses;
    }

    public String getName() {
        return name;
    }

    public Long getAccuracy() {
        return accuracy;
    }

    public Long getLine() {
        return line;
    }

    public List<OSClass> getOsClasses() {
        return osClasses;
    }
}
