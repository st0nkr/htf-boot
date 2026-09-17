package com.teto.domain.http;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

import java.util.Objects;

public class Http extends BaseTarget implements Comparable<Http> {
    @Meta(tag = Tag.Url, notnull = true)
    private String url;

    public Http(String name, Long pid, int level) {
        super(name, TargetType.Http.name(), pid, level);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Http o) {
        return getUrl().compareTo(o.getUrl());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Http http = (Http) o;
        return Objects.equals(url, http.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
