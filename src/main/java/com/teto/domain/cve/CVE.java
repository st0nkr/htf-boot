package com.teto.domain.cve;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CVE implements Comparable<CVE> {
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.CveId)
    private String cveId;
    @Meta(tag = Tag.ParentId)
    private Integer parentId;
    @Meta(tag = Tag.Description)
    private String description;
    @Meta(tag = Tag.TagWords)
    private String tagWords;
    @Meta(tag = Tag.Product)
    private String product;
    @Meta(tag = Tag.Vendor)
    private String vendor;
    @Meta(tag = Tag.Versions)
    private String versions;
    @Meta(tag = Tag.Version)
    private String version;
    @Meta(tag = Tag.ReferenceUrl)
    private String referenceUrl;
    @Meta(tag = Tag.Source)
    private String source;
    @Meta(tag = Tag.AttackVector)
    private String attackVector;
    @Meta(tag = Tag.Scope)
    private String scope;
    @Meta(tag = Tag.VectorString)
    private String vectorString;
    @Meta(tag = Tag.BaseScore)
    private Double baseScore;
    @Meta(tag = Tag.ImpactScore)
    private Double impactScore;
    @Meta(tag = Tag.ExploitabilityScore)
    private Double exploitabilityScore;
    @Meta(tag = Tag.TemporalScore)
    private Double temporalScore;
    @Meta(tag = Tag.EnvironmentScore)
    private Double environmentScore;
    @Meta(tag = Tag.ModifiedImpactScore)
    private Double modifiedImpactScore;
    @Meta(tag = Tag.PrivilegedRequired)
    private String privilegesRequired;
    @Meta(tag = Tag.Provenance)
    private Provenance provenance;
    @Meta(tag = Tag.UnderlyingCpe)
    private String underlyingCpe;
    @Meta(tag = Tag.UnderlyingSystem)
    private String underLyingSystem;
    @JsonIgnore
    private long portNumber;
    @Meta(tag = Tag.ParentType)
    private String parentType;
    @Meta(tag = Tag.Level)
    private Integer level;
    @Meta(tag = Tag.CVEType)
    private String cveType;

    public String getCveType() {
        return cveType;
    }

    public void setCveType(String cveType) {
        this.cveType = cveType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public CVE clone() {
        CVE c = new CVE();
        c.setCveId(getCveId());
        c.setVersion(getVersion());
        c.setDescription(getDescription());
        c.setTagWords(getTagWords());
        c.setProduct(getProduct());
        c.setVendor(getVendor());
        c.setVersions(getVersions());
        return c;
    }

    @Override
    public String toString() {
        return "CVE{" +
                "cveType='" + cveType + '\'' +
                ", cveId='" + cveId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getCveId() {
        return cveId;
    }

    public void setCveId(String cveId) {
        this.cveId = cveId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagWords() {
        return tagWords;
    }

    public void setTagWords(String tagWords) {
        this.tagWords = tagWords;
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

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAttackVector() {
        return attackVector;
    }

    public void setAttackVector(String attackVector) {
        this.attackVector = attackVector;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getVectorString() {
        return vectorString;
    }

    public void setVectorString(String vectorString) {
        this.vectorString = vectorString;
    }

    public Double getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(Double baseScore) {
        this.baseScore = baseScore;
    }

    public Double getImpactScore() {
        return impactScore;
    }

    public void setImpactScore(Double impactScore) {
        this.impactScore = impactScore;
    }

    public Double getExploitabilityScore() {
        return exploitabilityScore;
    }

    public void setExploitabilityScore(Double exploitabilityScore) {
        this.exploitabilityScore = exploitabilityScore;
    }

    public Double getTemporalScore() {
        return temporalScore;
    }

    public void setTemporalScore(Double temporalScore) {
        this.temporalScore = temporalScore;
    }

    public Double getEnvironmentScore() {
        return environmentScore;
    }

    public void setEnvironmentScore(Double environmentScore) {
        this.environmentScore = environmentScore;
    }

    public Double getModifiedImpactScore() {
        return modifiedImpactScore;
    }

    public void setModifiedImpactScore(Double modifiedImpactScore) {
        this.modifiedImpactScore = modifiedImpactScore;
    }

    public String getPrivilegesRequired() {
        return privilegesRequired;
    }

    public void setPrivilegesRequired(String privilegesRequired) {
        this.privilegesRequired = privilegesRequired;
    }

    public Provenance getProvenance() {
        return provenance;
    }

    public void setProvenance(Provenance provenance) {
        this.provenance = provenance;
    }

    public String getUnderlyingCpe() {
        return underlyingCpe;
    }

    public void setUnderlyingCpe(String underlyingCpe) {
        this.underlyingCpe = underlyingCpe;
    }

    public String getUnderLyingSystem() {
        return underLyingSystem;
    }

    public void setUnderLyingSystem(String underLyingSystem) {
        this.underLyingSystem = underLyingSystem;
    }

    public long getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(long portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public int compareTo( CVE o) {
        int cmp = cveId.compareTo(o.getCveId());
        if(cmp != 0) return cmp;
        if(getCveType() != null && o.getCveType() != null) {
            return getCveType().compareTo(o.getCveType());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CVE cve = (CVE) o;
        return Objects.equals(cveId, cve.cveId) && Objects.equals(cveType, cve.cveType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cveId, cveType);
    }
}
