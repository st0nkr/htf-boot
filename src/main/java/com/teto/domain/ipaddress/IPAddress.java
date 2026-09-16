package com.teto.domain.ipaddress;


import java.util.Objects;

public class IPAddress  {
    private final String ip;
    private String town;
    private String country;

    public IPAddress(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IPAddress ipAddress = (IPAddress) o;
        return Objects.equals(ip, ipAddress.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ip);
    }
}
