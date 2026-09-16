package com.teto.domain.web.button;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.web.Widget;
public class Button extends Widget {

    public Button(NodeLocator node) {
        super(HtmlTag.button, node);
    }
}
