package com.teto.domain.web.form;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.web.Widget;

import java.util.ArrayList;
import java.util.List;

public class Form extends Widget {
    private List<Widget> widgets = new ArrayList<>();
    public Form(NodeLocator node) {
        super(HtmlTag.form, node);
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }
}
