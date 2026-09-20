package com.teto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface ISet {

    default <X> Set<X> toSet(X...xs) {
        return new HashSet<>(Arrays.asList(xs));
    }
}
