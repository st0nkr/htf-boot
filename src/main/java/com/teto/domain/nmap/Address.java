package com.teto.domain.nmap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address  {
    private String addr;
    private String addrType;
    private String vendor;
    private String macAddr;

    public Address() {}
    public Address(String addr, String addrType, String mac, String vendor) {
        this.addr = addr;
        this.addrType = addrType;
        this.vendor = vendor;
        this.macAddr = mac;
    }
}
