package com.teto.domain.nmap;

public class Address  {
    private final String addr;
    private final String addrType;

    public Address(String addr, String addrType) {
        this.addr = addr;
        this.addrType = addrType;
    }

    public String getAddr() {
        return addr;
    }

    public String getAddrType() {
        return addrType;
    }
}
