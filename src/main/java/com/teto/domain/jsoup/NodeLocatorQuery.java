package com.teto.domain.jsoup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeLocatorQuery extends AbstractNodeLocator implements Serializable {
    private String jsoup;

    private boolean strictFilter = false;

    private List<FilterNode> filters = new ArrayList<>();

    private List<NodeLocatorQuery> childQueries = new ArrayList<>();
    public NodeLocatorQuery() {
        jsoup = "*";
    }

    public NodeLocatorQuery(Map<String, String> attrs) {
        for (String attr : attrs.keySet()) {
            put(attr, attrs.get(attr));
        }
    }

    public NodeLocatorQuery childQuery(NodeLocatorQuery q) {
        getChildQueries().add(q);
        return this;
    }

    public String getAttribute(String key) {
        return get(key);
    }
    public NodeLocatorQuery attribute(String key, String value) {
       put(key, value);

        return this;
    }

    public NodeLocatorQuery filter(FilterNode filter) {
        getFilters().add(filter);
        return this;
    }

    public String getJsoup() {
        return jsoup;
    }

    public void setJsoup(String jsoup) {
        this.jsoup = jsoup;
    }

    public boolean isStrictFilter() {
        return strictFilter;
    }

    public void setStrictFilter(boolean strictFilter) {
        this.strictFilter = strictFilter;
    }

    @Override
    public List<FilterNode> getFilters() {
        return filters;
    }

    @Override
    public void setFilters(List<FilterNode> filters) {
        this.filters = filters;
    }

    public List<NodeLocatorQuery> getChildQueries() {
        return childQueries;
    }

    public void setChildQueries(List<NodeLocatorQuery> childQueries) {
        this.childQueries = childQueries;
    }
}
