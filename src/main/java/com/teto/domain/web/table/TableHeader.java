package com.teto.domain.web.table;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.web.Widget;
public class TableHeader extends Widget {
    private String name;

    public TableHeader(NodeLocator node, String name) {
        this(node);
        this.name = name;
    }

    public TableHeader(NodeLocator node) {
        super(HtmlTag.th, node);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
