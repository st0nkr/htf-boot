package com.teto.domain.cpe;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

import java.util.Objects;

public class CPE extends BaseTarget implements Comparable<CPE>  {
    @Meta(tag = Tag.Uri)
    private String uri;

    public CPE(String name, Long pid, int level) {
        super(name, TargetType.CPE.name(), pid, level);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int compareTo(CPE o) {
        return getUri().compareTo(o.getUri());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CPE cpe = (CPE) o;
        return Objects.equals(uri, cpe.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uri);
    }
}
