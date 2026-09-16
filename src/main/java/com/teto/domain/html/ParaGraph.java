package com.teto.domain.html;

import com.teto.domain.jsoup.NodeLocator;

import java.util.List;

public class ParaGraph {
    private final NodeLocator node;
    private List<Code> codes;
    public ParaGraph(NodeLocator node) {
        this.node = node;
    }

    public NodeLocator getNode() {
        return node;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }
}
