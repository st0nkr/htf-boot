package com.teto.domain.ipaddress;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

import java.util.Objects;

public class Ipv6 extends BaseTarget implements Comparable<Ipv6> {
    @Meta(tag = Tag.IPAddress, notnull = true)
    private String ipAddress;

    public Ipv6(String name, Long pid, int level) {
        super(name, TargetType.Ipv6.name(), pid, level);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public int compareTo(Ipv6 o) {
        return getIpAddress().compareTo(o.getIpAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ipv6 ipv6 = (Ipv6) o;
        return Objects.equals(ipAddress, ipv6.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ipAddress);
    }
}
