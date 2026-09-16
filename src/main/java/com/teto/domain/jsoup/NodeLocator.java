package com.teto.domain.jsoup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeLocator extends AbstractNodeLocator implements Serializable {
    private Integer reponseCode;
    private transient List<FilterNode> filters = new ArrayList<>();
    private Collection<NodeLocator> children = new ArrayList<>();
    private Map<String, String> styles;
    private String name;
    private String xpath;

    private int index;
    private transient Object jsoupElement;
    private transient Object parent;

    public NodeLocator() {
    }
    public NodeLocator(Map<String, String> attrs) {
        setMeta(attrs);
    }

    public boolean hasChildren() {
        return getChildren() != null && !getChildren().isEmpty();
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public NodeLocator clone() throws CloneNotSupportedException {
        NodeLocator copy = (NodeLocator) super.clone();
        copy.setReponseCode(reponseCode);
        copy.setChildren(children);
        copy.setFilters(filters);
        return copy;
    }
    public boolean matches(NodeLocator node) {
        if(filters != null) {
            for (var filter : filters) {
                return filter.filter(node);
            }
        }
       return true;
    }

    public Integer getReponseCode() {
        return reponseCode;
    }

    public void setReponseCode(Integer reponseCode) {
        this.reponseCode = reponseCode;
    }

    @Override
    public List<FilterNode> getFilters() {
        return filters;
    }

    @Override
    public void setFilters(List<FilterNode> filters) {
        this.filters = filters;
    }

    @Override
    public Collection<NodeLocator> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Collection<NodeLocator> children) {
        this.children = children;
    }

    @Override
    public Map<String, String> getStyles() {
        return styles;
    }

    @Override
    public void setStyles(Map<String, String> styles) {
        this.styles = styles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getJsoupElement() {
        return jsoupElement;
    }

    public void setJsoupElement(Object jsoupElement) {
        this.jsoupElement = jsoupElement;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }
}
