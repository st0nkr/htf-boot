package com.teto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface IList {
    /* Arrays.asList() return value is not mofiable */
    default <X> List<X> asList(X...things) {
        final List<X> list = new ArrayList<>();
        for(X thing : things) {
            if(!thing.toString().isEmpty()) {
                list.add(thing);
            }
        }
        return list;
    }

    default <X> List<X> dedupe(List<X> things) {
        Set<X> set = new HashSet<>(things);
        return new ArrayList<>(set);
    }

    default String findShortest(List<String> things) {
        String shortest = null;
        for(String thing : things) {
            if(shortest == null || thing.length() < shortest.length()) {
                shortest = thing;
            }
        }
        return shortest;
    }
}
