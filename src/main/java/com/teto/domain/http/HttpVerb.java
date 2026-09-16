package com.teto.domain.http;

public enum HttpVerb {
    OPTIONS,
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    TRACE,
    CONNECT;

    public static HttpVerb fromString(String str) {
        for(HttpVerb verb : values()) {
            if(verb.name().equalsIgnoreCase(str)) {
                return verb;
            }
        }
        return null;
    }
}
