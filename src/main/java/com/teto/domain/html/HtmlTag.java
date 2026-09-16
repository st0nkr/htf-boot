package com.teto.domain.html;

import java.util.ArrayList;
import java.util.List;

public enum HtmlTag {
    html, head, title, base, link, meta, slot,style, script, noscript, template,
    symbol, circle,li,img,g,use,date, datetimelocal,week,tel,file,month,number,range,url,search,
    checkbox,password,color,radio,reset,
    body, section, nav, article, aside, h1, h2, h3, h4, h5, h6, header, footer,email,
    address, main, p, hr, pre,picture, plaintext,portal, blockquote, ol, ul, dl, dt, dd, figure, figcaption,
    div, a, em, strong, small, s, cite, q, dfn, abbr, data, time, code, var, samp, kbd,
    sub, sup, i, b, u, mark, ruby, rt, rp,rb,rtc, bdi, bdo, span, br, wbr, ins, del, path,
    iframe, embed, object, param, video, audio, source, track, canvas, map, area, svg, math,
    table, caption, colgroup, col, tbody, thead, tfoot, tr, td, th, tt, form, fieldset, legend,
    big,center,font,marquee,strike,acronym,applet,basefont,bgsound,blink,dir,frame,noframes,frameset,
    isindex,noframe,nobr,noembed,hgroup,xmp,submit,
    label, input, image, button, select, datalist, optgroup, option, text,textarea, keygen, output,
    progress, meter, details, dialog,summary, menuitem, menu, hidden, undefined,
    composite("vaadin-button");

    private String[] tags;
    HtmlTag() {}
    HtmlTag(String... tags) {
        this.tags = tags;
    }


    public static HtmlTag fromString(String str) {
        for (HtmlTag tag : values()) {
            if (tag.toString().equalsIgnoreCase(str)) {
                return tag;
            }
            if(tag.tags != null) {
                for(String loc : tag.tags) {
                    if(loc.equals(str)) {
                        return tag;
                    }
                }
            }
        }
        return null;
    }

    public static HtmlTag[] inputs() {
        return new HtmlTag[] { input, button, submit,select,
                textarea, menuitem, menu, image, link, a };
    }
    public static HtmlTag[] formInputs() {
        return new HtmlTag[] {
                button,
                input,
                textarea,
                select,
                optgroup,
        };
    }

    public static HtmlTag[] scripting() {
        return new HtmlTag[] { script, noscript, template };
    }

    public static HtmlTag[] sections() {
        return new HtmlTag[] { body, section, nav, article, aside, h1, h2, h3, h4, h5, h6, header, footer, address,
                main };
    }

    public static HtmlTag[] grouping() {
        return new HtmlTag[] { p, hr, pre, blockquote, ol, ul, li, dl, dt, dd, figure, figcaption, div };
    }


    public static HtmlTag[] tabular() {
        return new HtmlTag[] { table, caption, colgroup, col, tbody, thead, tfoot, tr, td, th };
    }

    public static HtmlTag[] forms() {
        return new HtmlTag[] { fieldset, legend, label, submit, input, button, select, datalist, optgroup, option,
                textarea, keygen, output, progress, meter };
    }

    public static HtmlTag[] interactive() {
        return new HtmlTag[] { details, summary, menuitem, menu };
    }

    public static String[] toStringArray(HtmlTag... values) {
        String[] ret = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i].toString();
        }
        return ret;
    }

    public static String[] formInputTags() {
        final List<String> tags = new ArrayList<>();
        for(HtmlTag tag : formInputs()) {
            tags.add(tag.name());
        }
        return tags.toArray(new String[0]);
    }

    public static String[] valuesAsString() {
        final List<String> tags = new ArrayList<>();
        for(HtmlTag tag : values()) {
            tags.add(tag.name());
        }
        return tags.toArray(new String[0]);
    }
}
