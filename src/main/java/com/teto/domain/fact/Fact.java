package com.teto.domain.fact;

import com.teto.domain.meta.Tag;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Fact implements Comparable<Fact>{
    private final Tag name;
    private final String value;
    private Integer confidence;

    public Fact(Tag name, String value) {
        this.name = name;
        this.value = value;
    }

    public Fact(Tag name, String value, Integer confidence) {
        this.name = name;
        this.value = value;
        this.confidence = confidence;
    }

    @Override
    public int compareTo(Fact o) {
        return name.compareTo(o.getName());
    }
}
