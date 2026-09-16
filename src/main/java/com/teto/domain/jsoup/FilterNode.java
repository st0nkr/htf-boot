package com.teto.domain.jsoup;

@FunctionalInterface
public interface FilterNode {
    boolean filter(NodeLocator loc);
}
