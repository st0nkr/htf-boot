package com.teto.domain.web;

import com.teto.domain.web.table.Table;
import org.jsoup.nodes.Document;

import java.util.List;

public class WebScrapeResponse {
    private String url;
    private Document doc;
    private List<Table> tables;

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

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}
