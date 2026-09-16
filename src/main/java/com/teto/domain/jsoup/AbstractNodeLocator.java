package com.teto.domain.jsoup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.*;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractNodeLocator implements Serializable {
    private Map<String, String> meta = new HashMap<>();
    protected transient Object element;
    protected transient List<FilterNode> filters = new ArrayList<>();
    protected Collection<NodeLocator> children = new ArrayList<>();
    protected Map<String, String> styles;

    public AbstractNodeLocator() {

    }
    public AbstractNodeLocator(Map<String, String> meta) {
        this.meta = meta;
    }

    public String get(String key) {
        return meta.get(key);
    }

    public boolean containsKey(String key) {
        return meta.containsKey(key);
    }
    public void put(String key, String value) {
        meta.put(key,value);
    }

    public void remove(String key) {
        meta.remove(key);
    }

    public String getAttribute(String key) {
        return meta.get(key);
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public List<FilterNode> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterNode> filters) {
        this.filters = filters;
    }

    public Collection<NodeLocator> getChildren() {
        return children;
    }

    public void setChildren(Collection<NodeLocator> children) {
        this.children = children;
    }

    public Map<String, String> getStyles() {
        return styles;
    }

    public void setStyles(Map<String, String> styles) {
        this.styles = styles;
    }
}
