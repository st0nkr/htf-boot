package com.teto.domain.target;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class
Target implements Comparable<Target> {
    @Meta(tag = Tag.ID, id = true, index = 2)
    private Integer id;
    @Meta(tag = Tag.ParentId, index = 2)
    private Integer parentId;
    @Meta(tag = Tag.CustomerId, index = 0)
    private Integer customerId;
    @Meta(tag = Tag.ProjectId, index = 1)
    private Integer projectId;
    @Meta(tag = Tag.Name, length = 32, index = 3, display = true)
    private String name;
    @Meta(tag = Tag.Uri, index = 4, display = true)
    private String uri;
    @Meta(tag = Tag.ParentUri, index = 4)
    private String parentUri;
    @Meta(tag = Tag.ParentType, index = 4)
    private String parentType;
    @Meta(tag = Tag.TargetType, index = 5, display = true)
    private String targetType;
    @Meta(tag = Tag.TargetMainType, index = 5)
    private String targetMainType;
    @Meta(tag = Tag.IPAddress, index = 6, display = true)
    private String ipAddress;
    @Meta(tag = Tag.Provenance, index = 6, display = true)
    private String provenance;
    @Meta(tag = Tag.Cpe,  index = 7, display = true)
    private String cpe;
    @Meta(tag = Tag.UnderlyingCpe,  index = 8)
    private String underlyingCpe;
    @Meta(tag = Tag.Version,index = 9)
    private String version;
    @Meta(tag = Tag.Description, index = 10)
    private String description;
    @Meta(tag = Tag.OsFamily, index = 11)
    private String osFamily;
    @Meta(tag = Tag.OsVersion, index = 12)
    private String osVersion;

    @Meta(tag = Tag.Product, index = 13, display = true)
    private String product;
    @Meta(tag = Tag.Vendor, index = 14)
    private String vendor;
    @Meta(tag = Tag.OSVendor,index = 15)
    private String osVendor;
    @Meta(tag = Tag.KeyWords,  index = 16)
    private String keyWords;
    @Meta(tag = Tag.PortNumber, index = 17,display=true)
    private Long portNumber;
    @Meta(tag = Tag.Reason, index = 18)
    private String reason;
    @Meta(tag = Tag.PortState, index = 19, display = true)
    private String portSate;
    @Meta(tag = Tag.PortProtocol, index = 20, display = true)
    private String portProtocol;

    @Meta(tag = Tag.Level, index = 17)
    private Integer level = 0;
    @Meta(tag = Tag.Verified, index = 19)
    private Boolean verified = false;
    @Meta(tag = Tag.Email, index = 18)
    private String email;
    @Meta(tag = Tag.ExtraInfo, index = 18)
    private String extraInfo;
    @Meta(tag = Tag.Score, index = 19)
    private Integer score = 0;
    @Meta(tag = Tag.OSAccuracy, index = 20)
    private Long osAccuracy = 0L;
    @Meta(tag = Tag.UserName, index = 21)
    private String userName;
    @Meta(tag = Tag.Password, index = 18)
    private String password;
    @Meta(tag = Tag.Alias, index = 18)
    private String alias;
    @Meta(tag = Tag.Platform, index = 18)
    private String platform;
    @Meta(tag = Tag.Location, index = 18)
    private String location;
    @Meta(tag = Tag.LinkedIn, index = 18)
    private String linkedIn;
    @Meta(tag = Tag.ICQ, index = 18)
    private String icq;
    @Meta(tag = Tag.Mobile, index = 22)
    private Boolean mobile;
    @Meta(tag = Tag.Company, index = 23)
    private String company;

    @Meta(tag = Tag.OSType, index = 21)
    private String osType;
    @Meta(tag = Tag.OSGen, index = 22)
    private String osGen;
    @Meta(tag = Tag.UnderlyingSystem, index = 23)
    private String underlyingSystem;
    @Meta(tag = Tag.StartDate, index = 4)
    protected Long startDate;
    @Meta(tag = Tag.EndDate, index = 5)
    protected Long endDate;
    @Meta(tag = Tag.TCPPorts, index=24)
    private String tcpPorts = "";
    @Meta(tag = Tag.UDPPorts,index = 24)
    private String udpPorts = "";
    @Meta(ignore = true)
    private transient String displayName;
    @Meta(tag = Tag.WebSite,index = 25)
    private String webSite;
    @Meta(tag = Tag.Confidence,index = 26)
    private Integer confidence;
    @Meta(tag = Tag.Icon,index = 27)
    private String icon;
    @Meta(tag = Tag.Slug,index = 29)
    private String slug;
    @Meta(tag = Tag.TriggerUrl,index = 30)
    private String triggerUrl;

    @Meta(tag = Tag.Manufacturer,index = 31)
    private String manufacturer;
    @Meta(tag = Tag.Severity,index = 32)
    private String severity;
    @Meta(tag = Tag.Type,index = 33)
    private String type;
    @Meta(tag = Tag.Type,index = 34)
    private String hostType;
    @Meta(tag = Tag.CpeVersion,index = 35)
    private String cpeVersion;
    @Meta(tag = Tag.Proxy,index = 36)
    private Boolean proxy = false;
    @Meta(tag = Tag.Reputation,index = 36)
    private String reputation;
    @Meta(tag = Tag.IXP,index = 37)
    private Boolean ixp = false;
    @Meta(tag = Tag.Bogon,index = 38)
    private Boolean bogon = false;
    @Meta(tag = Tag.NetRange,index = 36)
    private String netRange;
    @Meta(tag = Tag.NetName,index = 37)
    private String netName;
    @Meta(tag = Tag.Reverse,index = 38)
    private String reverse;
    @Meta(tag = Tag.OrgName,index = 39)
    private String orgName;
    @Meta(tag = Tag.AnyCast,index = 40)
    private Boolean anyCast;
    @Meta(tag = Tag.DC,index = 40)
    private Boolean dc;
    @Meta(tag = Tag.Url,index = 41)
    private String url;
    @Meta(tag = Tag.Data,index = 42)
    private String data;
    @Meta(tag = Tag.Module,index = 43)
    private String module;
    @JsonIgnore
    private transient String dorkQuery;
    @Meta(tag = Tag.City,index = 44)
    private String city;
    @Meta(tag = Tag.Region,index = 45)
    private String region;
    @Meta(tag = Tag.Country,index = 46)
    private String country;
    @Meta(tag = Tag.CountryCode,index = 47)
    private String countryCode;
    @Meta(tag = Tag.WildCardDNSEnabled,index = 48)
    private Boolean wildCardDnsEnabled = false;
    @Meta(tag = Tag.MTU,index = 49)
    private Integer mtu;
    @Meta(tag = Tag.ContentType,index = 50)
    private String contentType;
    @Meta(tag = Tag.StatusCode,index = 51)
    private Integer statusCode;
    @Meta(tag = Tag.Server,index = 52)
    private String server;
    @Meta(tag = Tag.Interface,index = 53)
    private String iface;
    @Meta(tag = Tag.SubNetMask,index = 54)
    private String subNetMask;

    public String getIface() {
        return iface;
    }

    public void setIface(String iface) {
        this.iface = iface;
    }

    public String getSubNetMask() {
        return subNetMask;
    }

    public void setSubNetMask(String subNetMask) {
        this.subNetMask = subNetMask;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public Boolean getWildCardDnsEnabled() {
        return wildCardDnsEnabled;
    }

    public void setWildCardDnsEnabled(Boolean wildCardDnsEnabled) {
        this.wildCardDnsEnabled = wildCardDnsEnabled;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Boolean getDc() {
        return dc;
    }

    public void setDc(Boolean dc) {
        this.dc = dc;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getIcq() {
        return icq;
    }

    public void setIcq(String icq) {
        this.icq = icq;
    }

    public String getDorkQuery() {
        return dorkQuery;
    }

    public void setDorkQuery(String dorkQuery) {
        this.dorkQuery = dorkQuery;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNetRange() {
        return netRange;
    }

    public void setNetRange(String netRange) {
        this.netRange = netRange;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Boolean getAnyCast() {
        return anyCast;
    }

    public void setAnyCast(Boolean anyCast) {
        this.anyCast = anyCast;
    }

    public String getCpeVersion() {
        return cpeVersion;
    }

    public void setCpeVersion(String cpeVersion) {
        this.cpeVersion = cpeVersion;
    }

    public Boolean getProxy() {
        return proxy;
    }

    public void setProxy(Boolean proxy) {
        this.proxy = proxy;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    public Boolean getIxp() {
        return ixp;
    }

    public void setIxp(Boolean ixp) {
        this.ixp = ixp;
    }

    public Boolean getBogon() {
        return bogon;
    }

    public void setBogon(Boolean bogon) {
        this.bogon = bogon;
    }

    public String getHostType() {
        return hostType;
    }

    public void setHostType(String hostType) {
        this.hostType = hostType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTriggerUrl() {
        return triggerUrl;
    }

    public void setTriggerUrl(String triggerUrl) {
        this.triggerUrl = triggerUrl;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getTcpPorts() {
        return tcpPorts;
    }

    public void setTcpPorts(String tcpPorts) {

        this.tcpPorts = tcpPorts;
    }

    public String getUdpPorts() {
        return udpPorts;
    }

    public void setUdpPorts(String udpPorts) {
        this.udpPorts = udpPorts;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTargetMainType() {
        return targetMainType;
    }

    public void setTargetMainType(String targetMainType) {
        this.targetMainType = targetMainType;
    }

    public String getParentUri() {
        return parentUri;
    }

    public void setParentUri(String parentUi) {
        this.parentUri = parentUi;
    }

    public String getUnderlyingSystem() {
        return underlyingSystem;
    }

    public void setUnderlyingSystem(String underlyingSystem) {
        this.underlyingSystem = underlyingSystem;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsGen() {
        return osGen;
    }

    public void setOsGen(String osGen) {
        this.osGen = osGen;
    }

    public Long getOsAccuracy() {
        return osAccuracy;
    }

    public void setOsAccuracy(Long osAccuracy) {
        this.osAccuracy = osAccuracy;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    private transient Collection<TargetProperty> properties = new HashSet<>();
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Collection<TargetProperty> getProperties() {
        return properties;
    }

    public void setProperties(Collection<TargetProperty> properties) {
        this.properties = properties;
    }

    private Collection<Target> children = new ArrayList<>();

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getOsVendor() {
        return osVendor;
    }

    public void setOsVendor(String osVendor) {
        this.osVendor = osVendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getUnderlyingCpe() {
        return underlyingCpe;
    }

    public void setUnderlyingCpe(String underlyingCpe) {
        this.underlyingCpe = underlyingCpe;
    }

    public String getCpe() {
        return cpe;
    }

    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public Collection<Target> getChildren() {
        return children;
    }

    public void setChildren(Collection<Target> children) {
        this.children = children;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }


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

    public Long getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Long portNumber) {
        this.portNumber = portNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPortSate() {
        return portSate;
    }

    public void setPortSate(String portSate) {
        this.portSate = portSate;
    }

    public String getPortProtocol() {
        return portProtocol;
    }

    public void setPortProtocol(String portProtocol) {
        this.portProtocol = portProtocol;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getProvenance() {
        return provenance;
    }

    public String getDisplayName() {
        if(displayName == null) {
            displayName = name;
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
                "targetType='" + targetType + '\'' +
                ", name='" + name + '\'' +
                ", cpe='" + cpe + '\'' +
                ", provenance='" + provenance + '\'' +
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
        int cmp = getUri().compareTo(o.getUri());
        if(cmp != 0) return cmp;
        String tt = getTargetType();
        if(tt != null && o.getTargetType() != null) {
            return tt.compareTo(o.getTargetType());
        }
        return 0;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, uri, parentUri, parentType, targetType, targetMainType, ipAddress, provenance, cpe, underlyingCpe, version, description, osFamily, osVersion, product, vendor, osVendor, keyWords, portNumber, reason, portSate, portProtocol, verified, email, extraInfo, score, osAccuracy, userName, password, alias, platform, location, linkedIn, icq, mobile, company, osType, osGen, underlyingSystem, startDate, endDate, tcpPorts, udpPorts, displayName, webSite, confidence, icon, slug, triggerUrl, manufacturer, severity, type, hostType, cpeVersion, proxy, reputation, ixp, bogon, netRange, netName, reverse, orgName, anyCast, dc, url, data, module, city, region, country, countryCode);
    }
}
