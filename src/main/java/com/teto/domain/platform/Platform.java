package com.teto.domain.platform;

public enum Platform {
    novell,
    multiple,
    linux,
    freebsd,
    unix,
    php,
    linux_x86,
    linux_x86_64("linux_x86-64"),
    hardware,
    webapps,
    windows_x86_64("windows_x86-64"),
    windows;

    private final String text;

     Platform() {
        text = name();
    }

    Platform(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public static Platform fromString(String str) {
        for(Platform p : values()) {
            if(p.getText().equalsIgnoreCase(str)) {
                return p;
            }
        }
        return null;
    }
}
