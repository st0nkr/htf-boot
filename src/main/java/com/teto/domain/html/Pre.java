package com.teto.domain.html;

import com.teto.domain.jsoup.NodeLocator;

import java.util.ArrayList;
import java.util.List;

public class Pre {
    private NodeLocator node;
    private List<?> children = new ArrayList<>();

    public NodeLocator getNode() {
        return node;
    }

    public void setNode(NodeLocator node) {
        this.node = node;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }
}
