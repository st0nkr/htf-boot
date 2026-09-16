package com.teto.domain;

import com.teto.domain.meta.Tag;

import java.util.HashMap;
import java.util.Map;

public class BaseAttributes {
    private Map<Tag, Object> attributes = new HashMap<>();

    public String get(Tag meta) {
        Object obj = attributes.get(meta);
        if(obj == null) {
            return null;
        }
        if(!(obj instanceof String)) {
            return obj.toString();
        } else {
            return (String) attributes.get(meta);
        }
    }

    public <X> X get(Tag meta, Class<X> clazz) {
        return (X) attributes.get(meta);
    }

    public Long getLong(Tag meta) {
        String val  = get(meta);
        try {
            return Long.parseLong(val.toString());
        } catch(Exception e) {
            return null;
        }
    }
    public Integer getInt(Tag meta) {
        String val  = get(meta);
        try {
            return Integer.parseInt(val);
        } catch(Exception e) {
            return null;
        }
    }

    public Boolean getBoolean(Tag meta) {
        String val  = get(meta);
        if(val != null) {
            try {
                return Boolean.parseBoolean(val);
            } catch(Exception e) {

            }
        }
        return Boolean.FALSE;
    }
    private boolean isStringyField(Object obj) {
        if((obj instanceof String) || (obj instanceof Integer)
                || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Double)) {
            return true;
        }
        return false;
    }
    public boolean put(Tag meta, Object value) {
        if(meta != null && value != null) {
            if(isStringyField(value)) {
                attributes.put(meta, value.toString());
            } else {
                attributes.put(meta, value);
            }
            return true;
        }
        return false;
    }

    public Map<Tag, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Tag, Object> attributes) {
        this.attributes = attributes;
    }
}
