package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

import java.util.List;

public class Table extends BaseEntity {
    private final String key;
    private final List<Elem> elems;
    private final List<Table> tables;

    public Table(String key, List<Elem> elems, List<Table> tables) {
        this.key = key;
        this.elems = elems;
        this.tables = tables;
    }

    public String getKey() {
        return key;
    }

    public List<Elem> getElems() {
        return elems;
    }

    public List<Table> getTables() {
        return tables;
    }
}
