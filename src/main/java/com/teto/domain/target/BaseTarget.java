package com.teto.domain.target;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

public abstract class BaseTarget {
    @Meta(id = true, tag = Tag.ID)
    private Integer id;
    @Meta(tag = Tag.Name)
    private String name;
    @Meta(tag = Tag.TargetType)
    private String targetType;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Integer parentId;
    @Meta(tag = Tag.ParentType)
    private String parentType;
    @Meta(tag = Tag.Level)
    private Integer level;
    @Meta(tag = Tag.Provenance)
    private String provenance;
    @Meta(tag = Tag.Created)
    private Long created;
    @Meta(tag = Tag.OsFamily)
    private String osFamily;
    @Meta(tag = Tag.Version)
    private String version;

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BaseTarget() {}

    public BaseTarget(String name, String targetType, Integer parentId, Integer level, Long created) {
        this.name = name;
        this.targetType = targetType;
        this.parentId = parentId;
        this.level = level;
        this.created = created;
    }
    public BaseTarget(String name, String targetType, Integer parentId, Integer level) {
        this(name, targetType,parentId,level,System.currentTimeMillis());
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getTargetType() {
        return targetType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getLevel() {
        return level;
    }


}
