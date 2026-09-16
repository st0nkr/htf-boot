package com.teto.domain.nvp;

public class NVP {
    private String name;
    private String value;

    public NVP() {}
    public NVP(String n, String v) {
        this.name = n;
        this.value = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
