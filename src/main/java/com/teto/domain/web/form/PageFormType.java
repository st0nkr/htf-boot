package com.teto.domain.web.form;

public enum PageFormType {
    Search,
    Login,
    Hidden,
    Unknown;

    public static PageFormType fromString(String str) {
        for(PageFormType pft : values()) {
            if(pft.name().equalsIgnoreCase(str)) {
                return pft;
            }
        }
        return null;
    }
}
