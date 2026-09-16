package com.teto.domain.web.table;

import com.teto.domain.html.HtmlTag;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.web.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table extends Widget {
    Map<Integer, String> columnNames = new HashMap<>();
    private List<TableHeader> headers = new ArrayList<>();
    private List<TableRow> rows = new ArrayList<>();

    public Table(NodeLocator node) {
        super(HtmlTag.table, node);
    }

    public String getColumnName(int idx) {
        return columnNames.get(idx);
    }
    public void setHeaders(List<TableHeader> headers) {
        this.headers = headers;
        for(Integer i = 0 ; i < headers.size() ; i++) {
            TableHeader header = headers.get(i);
            columnNames.put(i, header.getName());
        }
    }

    public Map<Integer, String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Map<Integer, String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<TableHeader> getHeaders() {
        return headers;
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void setRows(List<TableRow> rows) {
        this.rows = rows;
    }
}
