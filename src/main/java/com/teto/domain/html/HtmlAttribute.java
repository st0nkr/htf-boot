package com.teto.domain.html;

public enum HtmlAttribute {
    name,
    type,
    placeholder,
    clazz("class"),
    autocomplete,
    dataTextId("data-text-id"),
    arialabel("aria-label","ariaLable"),
    ariadescribedby,
    value;

    private String[] tags;

    private HtmlAttribute() {
        this.tags = null;
    }

    public String[] getTags() {
        return tags;
    }
    private HtmlAttribute(String...tags) {
        this.tags = tags;
    }

    public HtmlAttribute fromString(String str) {
        for(HtmlAttribute attr : values()) {
            if(attr.name().equalsIgnoreCase(str)) {
                return attr;
            }
            if(attr.tags != null) {
                for(String tag : attr.tags) {
                    if(tag.equalsIgnoreCase(str)) {
                        return attr;
                    }
                }
            }
        }
        return null;
    }
}
