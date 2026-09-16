package com.teto.domain.nmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teto.domain.BaseEntity;

/*
Pos  Implementation     Score  Hits
1    Squid2.5.STABLE5   60     28
        2    Squid2.5.STABLE9   60     28
        3    Squid2.6.STABLE13  60     28
        4    Apache1.3.26       56     30
        5    Apache2.0.54       56     28
        6    MicrosoftIIS6.0    56     29
        7    Apache2.2.6        54     28
        8    NetgearRP1143.26   53     27
        9    Zope2.7.5          53     27
        10   AOLserver3.4.2     52     28
 */
public class Tech extends BaseEntity {
    private int position;
    private String version;
    private int score;
    private int hits;
    @JsonIgnore
    private transient TechImpl impl;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public TechImpl getImpl() {
        return impl;
    }

    public void setImpl(TechImpl impl) {
        this.impl = impl;
    }
}
