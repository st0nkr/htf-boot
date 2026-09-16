package com.teto.domain.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teto.domain.BaseAttributes;
import com.teto.domain.nvp.NVP;
import com.teto.domain.web.form.PageForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class WebPage extends BaseAttributes {
    private String url;
    private List<PageForm> forms = new ArrayList<>();
    @JsonIgnore
    private transient Object document;
    private Map<String, String> queryParameters;
    private Collection<NVP> cookies = new ArrayList<>();
    private Collection<NVP> localStorage = new ArrayList<>();
    private Collection<NVP> sessionStorage = new ArrayList<>();

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public Collection<NVP> getCookies() {
        return cookies;
    }

    public void setCookies(Collection<NVP> cookies) {
        this.cookies = cookies;
    }

    public Collection<NVP> getLocalStorage() {
        return localStorage;
    }

    public void setLocalStorage(Collection<NVP> localStorage) {
        this.localStorage = localStorage;
    }

    public Collection<NVP> getSessionStorage() {
        return sessionStorage;
    }

    public void setSessionStorage(Collection<NVP> sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public Object getDocument() {
        return document;
    }

    public void setDocument(Object document) {
        this.document = document;
    }

    public String getUrl() {
        return url;
    }

    public WebPage setUrl(String url) {
        this.url = url;
        return this;
    }

    public List<PageForm> getForms() {
        return forms;
    }

    public WebPage setForms(List<PageForm> forms) {
        this.forms = forms;
        return this;
    }
}
