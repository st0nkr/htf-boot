package com.teto.domain.useragents;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class UserAgent extends BaseTarget implements Comparable<UserAgent> {
    @Meta(tag = Tag.Value)
    private String value;

    public UserAgent(String name, Long pid, int level) {
        super(name, TargetType.UserAgent.name(), pid, level);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public int compareTo(@NonNull UserAgent o) {
        return getValue().compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserAgent userAgent = (UserAgent) o;
        return Objects.equals(value, userAgent.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
