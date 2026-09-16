package com.teto.domain.web.input;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.web.Widget;
public class Input extends Widget {

    public Input(NodeLocator node) {
        super(HtmlTag.input, node);
    }
}
