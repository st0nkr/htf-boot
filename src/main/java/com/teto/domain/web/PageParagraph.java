package com.teto.domain.web;

import com.teto.domain.BaseEntity;

public class PageParagraph extends BaseEntity {
    private Integer order;
    private Integer indent;
    private String text;

    public PageParagraph(String text) {
        this.text = text;
    }

    public PageParagraph(String text, int indent, int order) {
        this.text = text;
        this.indent = indent;
        this.order = order;
    }

    @Override
    public String toString() {
        return "PageParagraph{" +
                "order=" + order +
                ", indent=" + indent +
                ", text='" + text + '\'' +
                '}';
    }
}
