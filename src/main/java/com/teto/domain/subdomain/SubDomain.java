package com.teto.domain.subdomain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class SubDomain extends BaseTarget implements Comparable<SubDomain> {
    @Meta(tag = Tag.Domain, notnull = true)
    private String domain;
    public SubDomain(String name, Integer pid, int level) {
        super(name, TargetType.SubDomain.name(), pid, level);
    }

    @Override
    public int compareTo(SubDomain o) {
        return getDomain().compareTo(o.getDomain());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubDomain subDomain = (SubDomain) o;
        return Objects.equals(domain, subDomain.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(domain);
    }

}
