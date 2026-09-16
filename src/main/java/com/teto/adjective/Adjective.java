package com.teto.adjective;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import org.jspecify.annotations.NonNull;

public class Adjective implements Comparable<Adjective> {
    @Meta(tag = Tag.ID, id = true, index = 0)
    private Integer id;
    @Meta(tag = Tag.Word, notnull = true, index = 1)
    private String word;
    @Meta(tag = Tag.Meaning, notnull = true, index = 2)
    private String meaning;
    @Meta(tag = Tag.Description, index = 3)
    private String description;

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(@NonNull Adjective o) {
        return getWord().compareTo(o.getWord());
    }
}
