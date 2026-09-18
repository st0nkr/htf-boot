package com.teto.domain.useragents;


import java.util.ArrayList;
import java.util.List;

public enum UserAgentEnum {
     // $Tag
    GoogleBot("GoogleBot"),
    MobileOk("W3C-mobileOK/DDC-1.0 (see http://www.w3.org/2006/07/mobileok-ddc)"),
    DefaultAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");

    private String value;
    UserAgentEnum(String value) {
        this.value = value;
    }

    public static String[] valueNames() {
        final List<String> names = new ArrayList<String>();
        for(final UserAgentEnum userAgent : values()) {
            names.add(userAgent.name());
        }
        return names.toArray(new String[0]);
    }

    public static UserAgentEnum fromString(String value) {
        for (UserAgentEnum userAgent : values()) {
            if(userAgent.name().equalsIgnoreCase(value)) {
                return userAgent;
            }
        }
        for (UserAgentEnum userAgent : values()) {
            if(userAgent.getValue().equalsIgnoreCase(value)) {
                return userAgent;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
