package com.teto.domain.nmap;

import com.teto.command.Context;
import com.teto.domain.BaseEntity;
import com.teto.domain.cve.CVE;
import com.teto.domain.port.ServicePort;
import com.teto.domain.product.Product;
import com.teto.domain.waf.WAF;

import java.util.List;

public class NSEScript  extends BaseEntity{
    private final String eyedee;
    private final String output;
    private final List<Elem> elems;
    private final List<Table> tables;
    private List<Product> techs;
    private List<WAF> wafs;
    private List<CVE> cves;
    private List<ServicePort> servicePorts;
    private Context ctx;
    private long portId;
    private NSEInfo info;
    private String serviceName;
    private Integer mtu;
    private String protocol;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<ServicePort> getServicePorts() {
        return servicePorts;
    }

    public void setServicePorts(List<ServicePort> servicePorts) {
        this.servicePorts = servicePorts;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public List<WAF> getWafs() {
        return wafs;
    }

    public void setWafs(List<WAF> wafs) {
        this.wafs = wafs;
    }

    public NSEScript(Context ctx, String portState, String protocol, Long portId, String id, String output, List<Elem> elems, List<Table> tables) {
        this.eyedee = id;
        this.output = output;
        this.elems = elems;
        this.tables = tables;
        this.ctx = ctx;
        this.portId = portId;
        this.protocol= protocol;

        if(output.startsWith("VulDB -")) {
            cves = new NSEScriptHelper().parseCVES(ctx, portId, output);
        } else {
            if(output.startsWith("IDS/IPS/WAF")) {
                wafs = new NSEScriptHelper().parseWaf(ctx, portId, output);
            } else {
                if(output.contains("PMTU")) {
                    mtu = new NSEScriptHelper().parseMtu(ctx,portId, output);
                }
                if(output.startsWith("Pos  Implementation    Score  Hits")) {
                   servicePorts = new NSEScriptHelper().parseServicePorts(ctx, portState, protocol, portId, output);
                } else {
                    techs = new NSEScriptHelper().parseTechs(ctx, portId, output);
                }
            }
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public NSEInfo getInfo() {
        return info;
    }

    public void setInfo(NSEInfo info) {
        this.info = info;
    }

    public String getEyedee() {
        return eyedee;
    }

    public String getOutput() {
        return output;
    }

    public List<Elem> getElems() {
        return elems;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Product> getTechs() {
        return techs;
    }

    public void setTechs(List<Product> techs) {
        this.techs = techs;
    }

    public List<CVE> getCves() {
        return cves;
    }

    public void setCves(List<CVE> cves) {
        this.cves = cves;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public long getPortId() {
        return portId;
    }

    public void setPortId(long portId) {
        this.portId = portId;
    }
}
