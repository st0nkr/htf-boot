package com.teto.domain.contact;


public class SocialMedia implements Comparable<SocialMedia>{
    private String value;

    public SocialMedia(String value) {
        this.value = value;
        assert value != null;
    }

    @Override
    public int compareTo(SocialMedia o) {
        return value.compareTo(o.getValue());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
