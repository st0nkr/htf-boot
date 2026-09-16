package com.teto.domain.regex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

import java.util.regex.Pattern;

public class Regex {
    @Meta(tag = Tag.ID, index = 0, id = true)
    private Integer id;
    @Meta(tag = Tag.Name, index = 1, notnull = true)
    private String name;
    @Meta(tag = Tag.Pattern, index = 2, notnull = true)
    private String pattern;
    @Meta(tag = Tag.KeyWords, index = 3, notnull = true)
    private String keyWords;
    @Meta(tag = Tag.TargetType, index = 4)
    private String targetType;

    @JsonIgnore
    private transient Pattern compile;
    public Regex(){}

    public Regex(String name, String pattern, String keyWords, String tt){
        this.name = name;
        this.pattern = pattern;
        this.keyWords = keyWords;
        this.targetType = tt;
    }

    public Pattern getCompile() {
        return compile;
    }

    public void setCompile(Pattern compile) {
        this.compile = compile;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
