package com.teto.domain.word;

public class Word {
    private final String original;
    private final String modified;

    public Word(String original, String modified) {
        this.original = original;
        this.modified = modified;
    }

    public String getOriginal() {
        return original;
    }

    public String getModified() {
        return modified;
    }
}
