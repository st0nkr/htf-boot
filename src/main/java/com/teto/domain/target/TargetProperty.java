package com.teto.domain.target;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetProperty implements Comparable<TargetProperty>{
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.TargetId, notnull = true)
    private Integer targetId;
    @Meta(tag = Tag.Name, notnull = true)
    private String name;
    @Meta(tag = Tag.Item, notnull = true)
    private String item;
    @Meta(tag = Tag.Description)
    private String description;

    public TargetProperty() {}
    public TargetProperty(Integer id, Integer targetId, String name, String value, String description) {
        this.id = id;
        this.targetId = targetId;
        this.name = name;
        this.item = value;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public int compareTo(TargetProperty o) {
        return getName().compareTo(o.getName());
    }
}
