package com.teto.domain.header;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import java.util.Objects;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Header implements Comparable<Header>{
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Integer parentId;
    @Meta(tag = Tag.Name, notnull = true)
    private String name;
    @Meta(tag = Tag.Value)
    private String value;
    @Meta(tag = Tag.Source)
    private String source;
    @Meta(tag = Tag.Provenance, notnull = true)
    private String provenance;
    @Meta(tag = Tag.Level)
    private Integer level;

    public Header() {}
    public Header(String name, String value, String provenance) {
        this.name = name;
        this.value = value;
        this.provenance = provenance;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
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

    @Override
    public int compareTo(Header o) {
        return name.compareTo(o.name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(id, header.id) && Objects.equals(parentId, header.parentId) && Objects.equals(name, header.name) && Objects.equals(value, header.value) && Objects.equals(source, header.source) && Objects.equals(provenance, header.provenance) && Objects.equals(level, header.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name, value, source, provenance, level);
    }
}
