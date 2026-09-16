package com.teto.domain.html;

import java.io.Serializable;

public class HtmlMeta implements Serializable {

    private HtmlType htmlType = HtmlType.HTML4;
    private String type;
    private String content;

    public HtmlType getHtmlType() {
        return htmlType;
    }

    public HtmlMeta setHtmlType(HtmlType htmlType) {
        this.htmlType = htmlType;
        return this;
    }

    public String getContent() {
        return content;
    }

    public HtmlMeta setContent(String content) {
        this.content = content;
        return this;
    }

    public String getType() {
        return type;
    }

    public HtmlMeta setType(String type) {
        this.type = type;
        return this;
    }
}
