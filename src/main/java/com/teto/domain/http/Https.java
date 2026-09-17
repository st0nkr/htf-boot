package com.teto.domain.http;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

import java.util.Objects;

public class Https extends BaseTarget implements Comparable<Https> {
    @Meta(tag = Tag.Url, notnull = true)
    private String url;

    public Https(String name, Long pid, int level) {
        super(name, TargetType.Https.name(), pid, level);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Https o) {
        return getUrl().compareTo(o.getUrl());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Https url1 = (Https) o;
        return Objects.equals(url, url1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
