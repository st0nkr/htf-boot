package com.teto.domain.url;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

import java.util.Objects;

public class Url extends BaseTarget implements Comparable<Url> {
    @Meta(tag = Tag.Url, notnull = true)
    private String url;
    @Meta(tag = Tag.Category)
    private String category;
    @Meta(tag = Tag.Description)
    private String description;
    @Meta(tag = Tag.Method)
    private String method;
    @Meta(tag = Tag.Body)
    private String body;
    @Meta(tag = Tag.Referer)
    private String referer;
    @Meta(tag = Tag.WSTG)
    private String wstg;
    @Meta(tag = Tag.Auth)
    private String auth;
    @Meta(tag = Tag.Module)
    private String module;
    @Meta(tag = Tag.Parameter)
    private String parameter;


    public Url(String name, Integer pid, int level) {
        super(name, TargetType.Url.name(), pid, level);
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getWstg() {
        return wstg;
    }

    public void setWstg(String wstg) {
        this.wstg = wstg;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Url o) {
        int cmp = getUrl().compareTo(o.getUrl());
        if(cmp != 0) return cmp;
        if(getParameter() != null && o.getParameter() != null) {
            cmp = getParameter().compareTo(o.getParameter());
            if(cmp != 0) return cmp;
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Url url1 = (Url) o;
        return Objects.equals(url, url1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
