package com.teto.domain.web.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class PageForm extends BaseTarget implements Comparable<PageForm> {
    @Meta(tag = Tag.Url, notnull = true)
    private String url;
    @Meta(tag = Tag.FormType, notnull = true)
    private String formType;
    @Meta(tag = Tag.cssSelector, notnull = true)
    private String cssSelector;
    @Meta(tag = Tag.Text)
    private String text;
    @Meta(tag = Tag.Method)
    private String method;
    private String query;
    @JsonIgnore
    private NodeLocator node;
    private Map<String, String> queryParameters;

    @Override
    public int compareTo(PageForm o) {
        return getUrl().compareTo(o.getUrl());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PageForm pageForm = (PageForm) o;
        return Objects.equals(url, pageForm.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
