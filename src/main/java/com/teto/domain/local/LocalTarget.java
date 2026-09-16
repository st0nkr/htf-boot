package com.teto.domain.local;

import com.teto.domain.target.Target;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Setter
@Getter
public class LocalTarget {
    private final Target target;
    private Collection<Target> services = new ArrayList<>();

    public LocalTarget(Target target) {
        this.target = target;
    }
}
