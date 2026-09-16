package com.teto.domain.web.table;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.web.Widget;
public class TableCell extends Widget {
    private String text;
    private String headerReference;

    public TableCell(NodeLocator node) {
        super(HtmlTag.td, node);
    }
    public TableCell(NodeLocator node, String text) {
       this(node);
       this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHeaderReference() {
        return headerReference;
    }

    public void setHeaderReference(String headerReference) {
        this.headerReference = headerReference;
    }
}
