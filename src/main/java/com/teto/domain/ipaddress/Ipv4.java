package com.teto.domain.ipaddress;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

import java.util.Objects;

public class Ipv4 extends BaseTarget implements Comparable<Ipv4> {
    @Meta(tag = Tag.IPAddress, notnull = true)
    private String ipAddress;

    public Ipv4(String name, Integer pid, int level) {
        super(name, TargetType.Ipv4.name(), pid, level);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public int compareTo(Ipv4 o) {
        return getIpAddress().compareTo(o.getIpAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ipv4 ipv4 = (Ipv4) o;
        return Objects.equals(ipAddress, ipv4.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ipAddress);
    }
}
