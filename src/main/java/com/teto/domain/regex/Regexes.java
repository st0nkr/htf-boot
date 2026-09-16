package com.teto.domain.regex;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class Regexes {
    private final Collection<Regex> regexes;

    public Regexes(Collection<Regex> regexes) {
        this.regexes = regexes;
    }

    public void add(Regex...regs) {
        regexes.addAll(Arrays.asList(regs));
    }
    public Iterator<Regex> iterator() {
        return regexes.iterator();
    }
}
