package com.teto.domain.web.page;

import com.teto.domain.web.Widget;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private String url;
    private Document doc;
    private List<Widget> forms = new ArrayList<>();
    private List<Widget> tables = new ArrayList<>();

    public Page() {}
    public Page(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public List<Widget> getForms() {
        return forms;
    }

    public void setForms(List<Widget> forms) {
        this.forms = forms;
    }

    public List<Widget> getTables() {
        return tables;
    }

    public void setTables(List<Widget> tables) {
        this.tables = tables;
    }
}
