package com.teto.domain.web;

import com.teto.domain.target.Target;

public class WebContext {
    private final Target parent;
    private final String parentUrl;
    private String userName;
    private String password;
    private String userNameWordList;
    private String passwordWordList;

    private String url;
    private WebPage page;

    public WebContext(Target parent, String parentUrl) {
        this.parentUrl = parentUrl;
        this.parent = parent;
    }

    public String getUserNameWordList() {
        return userNameWordList;
    }

    public void setUserNameWordList(String userNameWordList) {
        this.userNameWordList = userNameWordList;
    }

    public String getPasswordWordList() {
        return passwordWordList;
    }

    public void setPasswordWordList(String passwordWordList) {
        this.passwordWordList = passwordWordList;
    }

    public Target getParent() {
        return parent;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebPage getPage() {
        return page;
    }

    public void setPage(WebPage page) {
        this.page = page;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
