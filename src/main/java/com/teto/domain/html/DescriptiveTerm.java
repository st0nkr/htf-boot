package com.teto.domain.html;

import com.teto.domain.jsoup.NodeLocator;

public class DescriptiveTerm {
    private final NodeLocator node;
    private DescriptiveDetail detail;
    public DescriptiveTerm(NodeLocator node) {
        this.node = node;
    }

    public NodeLocator getNode() {
        return node;
    }

    public DescriptiveDetail getDetail() {
        return detail;
    }

    public void setDetail(DescriptiveDetail detail) {
        this.detail = detail;
    }
}
