package com.teto.domain.nmap;

public class Script {
    private String id;
    private String output;
    private Integer mtu;

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getId() {
        return id;
    }

    public String getOutput() {
        return output;
    }
}
