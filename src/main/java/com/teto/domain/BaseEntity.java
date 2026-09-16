package com.teto.domain;

import com.teto.domain.meta.Tag;

import java.util.HashMap;
import java.util.Map;

public class BaseEntity {
    private Map<String, Object> attributes;

    public Map<String, Object> getAttributes() {
        if(attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    public void put(String action, Object type) {
        getAttributes().put(action, type);
    }
    public void put(Tag m, Object value) {
        put(m.name(), value);
    }

    public String getString(String key) {
        return getAttributes().get(key).toString();
    }

    public String getString(Tag m) {
        return getString(m.name());
    }

    public <X> X get(Tag m, Class<X> type) {
        return (X) getAttributes().get(m.name());
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
