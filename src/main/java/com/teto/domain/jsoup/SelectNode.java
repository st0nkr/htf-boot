package com.teto.domain.jsoup;

@FunctionalInterface
public interface SelectNode {
    boolean filter(NodeLocator loc);
}
