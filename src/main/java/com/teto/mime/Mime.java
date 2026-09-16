package com.teto.mime;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

public class Mime implements Comparable<Mime>{
    @Meta(id = true, tag = Tag.ID, index = 0)
    private Integer id;
    @Meta(tag = Tag.Name, notnull = true, index = 1)
    private String name;
    @Meta(tag = Tag.Type, index = 2)
    private String type;
    @Meta(tag = Tag.SubType, index = 3)
    private String subType;
    @Meta( tag = Tag.Description, index = 4)
    private String description;
    @Meta(tag = Tag.KeyWords, index = 5)
    private String keywords;

    public Mime() {}
    public Mime(String name, String type, String subType, String description, String keywords) {
        this.name = name;
        this.type = type;
        this.subType = subType;
        this.description = description;
        this.keywords = keywords;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


    @Override
    public int compareTo(Mime o) {
        return getName().compareTo(o.getName());
    }
}
