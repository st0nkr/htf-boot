package com.teto.domain.cloud;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

import java.util.Objects;

public class StorageBucket implements Comparable<StorageBucket>{
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Integer parentId;
    @Meta(tag = Tag.Level)
    private Integer level;
    @Meta(tag = Tag.ParentType)
    private String parentType;
    @Meta(tag = Tag.Provenance, notnull = true)
    private String provenance;
    @Meta(tag = Tag.Uri)
    private String uri;
    @Meta(tag = Tag.Host)
    private String host;
    @Meta(tag = Tag.PortNumber)
    private Integer portNumber;
    @Meta(tag = Tag.Module)
    private String module;
    @Meta(tag = Tag.Description)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int compareTo(StorageBucket o) {
        return getUri().compareTo(o.getUri());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StorageBucket that = (StorageBucket) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(level, that.level) && Objects.equals(parentType, that.parentType) && Objects.equals(provenance, that.provenance) && Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, level, parentType, provenance, uri);
    }
}
