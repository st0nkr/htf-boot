package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class ScanInfo extends BaseEntity {
    private  String type;
    private  String protocol;
    private  Long numServices;
    private  String services;

    public ScanInfo() {}
    public ScanInfo(String type, String protocol, Long numServices, String services) {
        this.type = type;
        this.protocol = protocol;
        this.numServices = numServices;
        this.services = services;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getNumServices() {
        return numServices;
    }

    public void setNumServices(Long numServices) {
        this.numServices = numServices;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }
}
