package com.teto.domain.html;

import com.teto.domain.jsoup.NodeLocator;

import java.util.ArrayList;
import java.util.List;

public class DescriptiveList {
    private final NodeLocator node;
    private List<DescriptiveTerm> children = new ArrayList<>();

    public DescriptiveList(NodeLocator node) {
        this.node = node;
    }

    public NodeLocator getNode() {
        return node;
    }

    public List<DescriptiveTerm> getChildren() {
        return children;
    }

    public void setChildren(List<DescriptiveTerm> children) {
        this.children = children;
    }
}
