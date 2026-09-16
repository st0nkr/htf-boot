package com.teto.domain.acronym;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

public class Acronym {
    @Meta(id = true, tag = Tag.ID, index = 0)
    private Integer id;
    @Meta(tag = Tag.Acronym, notnull = true, index = 1)
    private String acronym;
    @Meta(tag = Tag.Substitute, notnull = true, index = 2)
    private String substitute;
    @Meta(tag = Tag.Description, notnull = true, index = 4)
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getSubstitute() {
        return substitute;
    }

    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Acronym{" +
                "id=" + id +
                ", acronym='" + acronym + '\'' +
                ", substitute='" + substitute + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
