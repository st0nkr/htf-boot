package com.teto.domain.file;

public enum FileExtension {
    json,
    txt,
    csv,
    html,
    yml,yaml,
    xml;

    public static FileExtension fromString(String str) {
        for(FileExtension fex : values()) {
            if(fex.name().equalsIgnoreCase(str)) {
                return fex;
            }
        }
        return null;
    }
}
