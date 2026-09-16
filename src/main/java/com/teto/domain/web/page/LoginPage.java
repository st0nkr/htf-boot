package com.teto.domain.web.page;

import com.teto.domain.BaseEntity;
import com.teto.domain.jsoup.NodeLocator;

public class LoginPage extends BaseEntity {
    private  String url;
    private  NodeLocator form;
    private  NodeLocator user;
    private  NodeLocator password;
    private  NodeLocator sumbit;

    public LoginPage() {}

    public LoginPage(String url, NodeLocator form, NodeLocator user, NodeLocator password, NodeLocator sumbit) {
        this.url = url;
        this.form = form;
        this.user = user;
        this.password = password;
        this.sumbit = sumbit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NodeLocator getForm() {
        return form;
    }

    public void setForm(NodeLocator form) {
        this.form = form;
    }

    public NodeLocator getUser() {
        return user;
    }

    public void setUser(NodeLocator user) {
        this.user = user;
    }

    public NodeLocator getPassword() {
        return password;
    }

    public void setPassword(NodeLocator password) {
        this.password = password;
    }

    public NodeLocator getSumbit() {
        return sumbit;
    }

    public void setSumbit(NodeLocator sumbit) {
        this.sumbit = sumbit;
    }
}
