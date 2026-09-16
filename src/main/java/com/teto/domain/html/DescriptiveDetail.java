package com.teto.domain.html;

import com.teto.domain.jsoup.NodeLocator;

import java.util.List;

public class DescriptiveDetail {
    private final NodeLocator node;
    private List<ParaGraph> paragraphs;
    public DescriptiveDetail(NodeLocator node) {
        this.node = node;
    }

    public NodeLocator getNode() {
        return node;
    }

    public List<ParaGraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<ParaGraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
}
