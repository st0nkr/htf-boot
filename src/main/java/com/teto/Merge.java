package com.teto;

public class Merge implements IMerge {

    public static <T> T mergePojos(T target, T source) {
        return new Merge().merge(target, source);
    }

    public static <T> T mergeObjects(T target, T source) {
        return new Merge().merge(target, source);
    }
}
