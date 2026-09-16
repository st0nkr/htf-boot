package com.teto.domain.http;

import java.io.Serializable;

public class HttpClientResponse implements Serializable {
    private int statusCode;
    private String source;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
