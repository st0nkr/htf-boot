package com.teto.domain.nmap;

import us.springett.parsers.cpe.Cpe;

public class Service  {
    private String name;
    private String product;
    private String extraInfo;
    private String tunnel;
    private String method;
    private String conf;
    private String serviceFP;
    private Long portId;
    private String protocol;
    private State state;
    private int targetId;
    private Cpe[] cpes;

    public Service(String name, String product, String extraInfo, String tunnel, String method, String conf, String serviceFP, Cpe... cpes) {
        this.name = name;
        this.product = product;
        this.extraInfo = extraInfo;
        this.tunnel = tunnel;
        this.method = method;
        this.conf = conf;
        this.serviceFP = serviceFP;
        this.cpes = cpes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public void setServiceFP(String serviceFP) {
        this.serviceFP = serviceFP;
    }

    public Cpe getCpe(int i) {
        if(cpes != null) {
            if(cpes.length > i) {
                return cpes[i];
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getProduct() {
        return product;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getTunnel() {
        return tunnel;
    }

    public String getMethod() {
        return method;
    }

    public String getConf() {
        return conf;
    }

    public String getServiceFP() {
        return serviceFP;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public Cpe[] getCpes() {
        return cpes;
    }

    public void setCpes(Cpe[] cpes) {
        this.cpes = cpes;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                '}';
    }
}
