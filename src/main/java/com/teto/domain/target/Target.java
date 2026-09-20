package com.teto.domain.target;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.Answer;
import com.teto.domain.annotation.Meta;
import com.teto.domain.difficulty.Difficulty;
import com.teto.domain.meta.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Target implements Comparable<Target> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private Integer customerId;
    private Integer projectId;
    private String name;
    private String uri;
    private String parentUri;
    private String parentType;
    private String targetType;
    private String targetMainType;
    private String ipAddress;
    private String provenance;
    private String cpe;
    private String underlyingCpe;
    private String version;
    private String description;
    private String osFamily;
    private String osVersion;

    private String product;
    private String vendor;
    private String osVendor;
    private String keyWords;
    private Long portNumber;
    private String reason;
    private String portSate;
    private String portProtocol;

    private Integer level = 0;
    private Boolean verified = false;
    private String email;
    private String extraInfo;
    private Integer score = 0;
    private Long osAccuracy = 0L;
    private String userName;
    private String password;
    private String alias;
    private String platform;
    private String location;
    private String linkedIn;
    private String icq;
    private Boolean mobile;
    private String company;

    private String osType;
    private String osGen;
    private String underlyingSystem;
    protected Long startDate;
    protected Long endDate;
    private String tcpPorts = "";
    private String udpPorts = "";
    private String webSite;
    private Integer confidence;
    private String icon;
    private String slug;
    private String triggerUrl;

    private String manufacturer;
    private String severity;
    private String type;
    private String hostType;
    private String cpeVersion;
    private Boolean proxy = false;
    private String reputation;
    private Boolean ixp = false;
    private Boolean bogon = false;
    private String netRange;
    private String netName;
    private String reverse;
    private String orgName;
    private Boolean anyCast;
    private Boolean dc;
    private String url;
    private String data;
    private String module;
    private String dorkQuery;
    private String city;
    private String region;
    private String country;
    private String countryCode;
    private Boolean wildCardDnsEnabled = false;
    private Integer mtu;
    private String contentType;
    private Integer statusCode;
    private String server;
    private String iface;
    private String subNetMask;
    private boolean suitableForIdleScan;
    private boolean suitableForZombieScan;
    private String difficulty;
    private String macAddress;

    private String kernalName;
    private String nodeName;
    private String kernelRelease;
    private String kernelVersion;
    private String machineName;
    private String processors;
    private String hardwarePlatform;
    private String osName;

    public static Target create() {
        Target t = new Target();
        return t;
    }

    public static Target create(String provenance) {
        Target t = create();
        t.setProvenance(provenance);
        return t;
    }

    public static Target create(String name, String source) {
        Target t = new Target();
        t.setName(name);
        t.setUri(source);
        return t;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        String source = getUri();
        String tt = getTargetType();
        return Objects.equals(source, target.getUri()) && Objects.equals(tt, target.getTargetType());
    }

    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", targetType='" + targetType + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", vendor='" + vendor + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Target o) {
        if(getUri() == null) {
            return -1;
        }
        if(o.getUri() == null) {
            return 1;
        }
        if(getPortNumber() != null && o.getPortNumber() != null) {
            if (getPortNumber() < o.getPortNumber()) {
                return -1;
            }
            if (getPortNumber() > o.getPortNumber()) {
                return 1;
            }
        }
        int cmp = 0;
        if(getPortProtocol() != null && o.getPortProtocol() != null) {
            cmp = getPortProtocol().compareTo(o.getPortProtocol());

            if (cmp != 0) return cmp;
        }
        if(getTargetType() != null && o.getTargetType() != null) {
            cmp = getTargetType().compareTo(o.getTargetType());

            if (cmp != 0) return cmp;
        }
        cmp = getUri().compareTo(o.getUri());
        return cmp;
    }
    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, uri, parentUri, parentType, targetType, targetMainType, ipAddress, provenance, cpe, underlyingCpe, version, description, osFamily, osVersion, product, vendor, osVendor, keyWords, portNumber, reason, portSate, portProtocol, verified, email, extraInfo, score, osAccuracy, userName, password, alias, platform, location, linkedIn, icq, mobile, company, osType, osGen, underlyingSystem, startDate, endDate, tcpPorts, udpPorts, webSite, confidence, icon, slug, triggerUrl, manufacturer, severity, type, hostType, cpeVersion, proxy, reputation, ixp, bogon, netRange, netName, reverse, orgName, anyCast, dc, url, data, module, city, region, country, countryCode);
    }
}
