package com.teto.domain.html;


import com.teto.domain.jsoup.NodeLocator;

public class Code {
    private final NodeLocator node;

    public Code(NodeLocator node) {
        this.node = node;
    }

    public NodeLocator getNode() {
        return node;
    }
}
