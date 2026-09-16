package com.teto.domain.product;

import com.teto.domain.BaseEntity;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.TargetType;

public class Product extends BaseEntity {
    private String name;
    private Long portId;
    private String version;
    private Provenance provenance;
    private TargetType targetType;
    private  int score;
    private int hits;
    private long portNumber;
    private String os;
    private String cpe;


    public Product() {}
    public Product(String name, String version, Provenance context) {
        this.name = name;
        this.version = version;
        this.provenance = context;
    }

    public String getCpe() {
        return cpe;
    }

    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Provenance getProvenance() {
        return provenance;
    }

    public void setProvenance(Provenance provenance) {
        this.provenance = provenance;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public long getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(long portNumber) {
        this.portNumber = portNumber;
    }
}
