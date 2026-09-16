package com.teto.domain.web.table;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.web.Widget;

import java.util.*;

public class TableRow extends Widget {
    private List<TableCell> cells = new ArrayList<>();;
    Map<String, String> attributes = new HashMap<>();

    public TableRow(NodeLocator node) {
        super(HtmlTag.tr, node);
    }
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    public String getAttributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    public void putAttribute(String attributeName, String attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    public List<TableCell> getCells() {
        return cells;
    }

    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
