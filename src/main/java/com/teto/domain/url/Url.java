package com.teto.domain.url;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Url implements Comparable<Url> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String category;
    private String description;
    private String method;
    private String body;
    private String referer;
    private String wstg;
    private String auth;
    private String module;
    private String parameter;
    private Integer responseCode;
    private Integer size;
    private String provenance;
    private Long parentId;
    private Integer level;
    private String parentType;
    private Integer contentLength;
    private String contentType;
    private String redirect;
    private Integer status;
    private String message;
    private String refs;
    private String eyeDee;
    @Override
    public String toString() {
        return "Url{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", responseCode=" + responseCode +
                ", size=" + size +
                ", provenance='" + provenance + '\'' +
                ", parentType='" + parentType + '\'' +
                ", level=" + level +
                '}';
    }

    @Override
    public int compareTo(Url o) {
        if(getUrl() != null && o.getUrl() != null) {
            return getUrl().compareTo(o.getUrl());
        }
        if(getName() != null && o.getName() != null) {
            return getName().compareTo(o.getName());
        }
        if(getProvenance() != null && o.getProvenance() != null) {
            return getProvenance().compareTo(o.getProvenance());
        }
        return 0;
    }
}
