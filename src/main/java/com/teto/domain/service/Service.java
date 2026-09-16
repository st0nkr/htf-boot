package com.teto.domain.service;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

import java.util.Objects;

public class Service extends BaseTarget implements Comparable<Service> {

    @Meta(tag = Tag.IPAddress)
    private String ipAddress;
    @Meta(tag = Tag.Protocol)
    private String protocol;
    @Meta(tag = Tag.PortNumber)
    private Long port;
    @Meta(tag = Tag.PortState)
    private String state;
    @Meta(tag = Tag.Cpe)
    private String cpe;
    @Meta(tag = Tag.Description)
    private String description;
    @Meta(tag = Tag.ExtraInfo)
    private String extraInfo;
    @Meta(tag = Tag.Product)
    private String product;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Service() {}

    public Service(String name, Integer parentId, int level) {
        super(name, TargetType.Service.name(), parentId, level);
    }

    public String getCpe() {
        return cpe;
    }

    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getPort() {
        return port;
    }

    @Override
    public int compareTo(Service o) {
        int cmp = getIpAddress().compareTo(o.getIpAddress());
        if(cmp != 0) return cmp;
        if(getPort() != null && o.getPort() != null) {
            if(getPort() < o.getPort()) return -1;
            if(getPort() > o.getPort()) return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(ipAddress, service.ipAddress) && Objects.equals(protocol, service.protocol) && Objects.equals(port, service.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, protocol, port);
    }

    @Override
    public String toString() {
        return "Service{" +
                "ipAddress='" + ipAddress + '\'' +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                '}';
    }
}
