package com.teto.domain.web;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
public abstract class Widget {
    private NodeLocator node;
    private HtmlTag tag;

    public Widget(HtmlTag tag, NodeLocator node) {
        this.node = node;
        this.tag = tag;
    }

    public NodeLocator getNode() {
        return node;
    }

    public void setNode(NodeLocator node) {
        this.node = node;
    }

    public HtmlTag getTag() {
        return tag;
    }

    public void setTag(HtmlTag tag) {
        this.tag = tag;
    }
}
