package com.teto.domain.target;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseTarget {
    @Meta(id = true, tag = Tag.ID)
    private Integer id;
    @Meta(tag = Tag.Name)
    private String name;
    @Meta(tag = Tag.TargetType)
    private String targetType;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Long parentId;
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

    public BaseTarget() {}

    public BaseTarget(String name, String targetType, Long parentId, Integer level, Long created) {
        this.name = name;
        this.targetType = targetType;
        this.parentId = parentId;
        this.level = level;
        this.created = created;
    }
    public BaseTarget(String name, String targetType, Long parentId, Integer level) {
        this(name, targetType,parentId,level,System.currentTimeMillis());
    }

}
