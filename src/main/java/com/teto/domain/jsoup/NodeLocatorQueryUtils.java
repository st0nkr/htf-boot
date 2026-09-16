package com.teto.domain.jsoup;

import com.teto.domain.html.HtmlAttribute;
import com.teto.domain.meta.ConfigProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class NodeLocatorQueryUtils  {

    public static Map<String,String> locatorToMap(String locator) {
        String[] nvps = locator.split(",");
        final Map<String,String> map = new HashMap<>();
        for(String nvp : nvps) {
            int idx = nvp.indexOf("(");
            String tag = nvp.substring(0,idx);
            int end = nvp.lastIndexOf(")");
            String val = nvp.substring(idx+1, end);
            map.put(tag,val);
        }
        return map;
    }
    public static NodeLocatorQuery query(String locator) {
        NodeLocatorQuery query = query();
        String[] nvps = locator.split(",");
        String tagged = null;
        for(String nvp : nvps) {
            int idx = nvp.indexOf("(");
            String tag = nvp.substring(0,idx);
            int end = nvp.lastIndexOf(")");
            String val = nvp.substring(idx+1, end);
            if(tag.equals(ConfigProperty.TAG)) {
               tagged = val;
            }
            // By lookup are quicker than filter (generally)
            if(ConfigProperty.CSS_SELECTOR.equals(tag)
                    || ConfigProperty.XPATH.equals(tag)
                    || ConfigProperty.ID.equals(tag)
                    || ConfigProperty.TAG.equals(tag)
                    || ConfigProperty.TEXT.equals(tag)) {
                query.put(tag, val);
                tag(query, tag,val);
            } else {
                tag(query, tag,val);
            }
        }
        if(query.getJsoup().equals("*")) {
           if(tagged != null) {
               query.setJsoup(tagged);
           }
        }
        return query;
    }
    public static NodeLocatorQuery query() {
        return new NodeLocatorQuery();
    }

    public static NodeLocatorQuery tag(String name, String val) {
        NodeLocatorQuery query = query();
        createMatches(query, name, val );
        return query;
    }

    public static NodeLocatorQuery tag(NodeLocatorQuery q, String name, String val) {
        createMatches(q, name, val);
        return q;
    }

    public static NodeLocatorQuery tag(String tag) {
        NodeLocatorQuery q = query();
        createMatches(q, ConfigProperty.TAG, tag);
        q.setJsoup(tag);
        return q;
    }

    public static NodeLocatorQuery tag(NodeLocatorQuery query, String tag) {
        query .setJsoup(tag);
        createMatches(query, ConfigProperty.TAG, tag);
        query.put(ConfigProperty.TAG, tag);
        return query;
    }

    public static NodeLocatorQuery xpath() {
        NodeLocatorQuery query = new NodeLocatorQuery();
        query.put(ConfigProperty.XPATH, ConfigProperty.TRUE);
        return query;
    }

    public static SelectNode select(final String key) {
        return loc -> {
            return loc.containsKey(key);
        };
    }

    public static void equals(NodeLocatorQuery query, String key, String text) {
        tag(query, key, text);
        createMatches(query, key, text);
    }

    public static void contains(NodeLocatorQuery query, HtmlAttribute attr, String text) {
        query.getFilters().add(loc -> {
            for(String tag : attr.getTags()) {
                String val = loc.getAttribute(tag);
                if (val != null && !val.isEmpty()) {
                    if(val.contains(text)) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    public static boolean strMatches(String pattern, String actual) {

        if(pattern.startsWith("*") && pattern.endsWith("*")) {
            int idx = pattern.lastIndexOf("*");
            String str = pattern.substring(1, idx);
            return actual.contains(str);
        }
        if(pattern.startsWith("*") && !pattern.endsWith("*")) {
            String str = pattern.substring(1);
            return actual.endsWith(str);
        }
        if(!pattern.startsWith("*") && pattern.endsWith("*")) {
            int idx = pattern.lastIndexOf("*");
            String str = pattern.substring(0, idx);
            return actual.startsWith(str);
        }
        if(actual == null) {
            return false;
        }
        return pattern.compareTo(actual) == 0;
    }
    public static void createMatches(NodeLocatorQuery query, String key, String part) {
        query.getFilters().add(loc -> {
            if(loc != null) {
                String val = loc.getAttribute(key);
                boolean ret = strMatches(part, val);
                return ret;
            }
            return true;
        });
    }

    public static Map<String, String> getLocatorMap(String[] nvps) {
        final Map<String, String> map = new HashMap<>();
        for (String nvp : nvps) {
            if(nvp != null && !nvp.isBlank()) {
                int start = nvp.indexOf("(");
                int end = nvp.lastIndexOf(")");
                String key = nvp.substring(0, start);
                String val = nvp.substring(start + 1, end);
                map.put(key, val);
            }
        }
        return map;
    }
    public static NodeLocatorQuery fromLocator(String[] nvps) {
        NodeLocatorQuery query =  query();
        Map<String, String> map = getLocatorMap(nvps);
        String tag = map.get(ConfigProperty.TAG);
        String text = map.get(ConfigProperty.TEXT);
        if(tag != null && text != null) {
            // This makes lookup quicker
            query.setJsoup(tag);
            // Dont use :contains as it's behaviour is not right
            // Relay on text checking through filter below
            createMatches(query, ConfigProperty.TEXT, text);
            map.remove(ConfigProperty.TAG);
            map.remove(ConfigProperty.TEXT);
        } else {
            if(tag != null && !tag.isEmpty()) {
                query.setJsoup(tag);
                map.remove(ConfigProperty.TAG);
            } else {
                query.setJsoup("*");
            }
        }
        for(String key : map.keySet()) {
            String val = map.get(key);
            if(ConfigProperty.ID.equals(key)) {
                query.put(key, val);
            }
            if(val != null && !val.isEmpty()) {
                createMatches(query, key, val);
            }
        }
        return query;
    }

    public static boolean isEmpty(String str) {
        if(str == null || str.strip().isEmpty()) {
            return true;
        }
        return false;
    }

    public static String createStringIdentifier(NodeLocator node) {
        StringBuilder sb = new StringBuilder();
        var keys = new ArrayList<>(node.getMeta().keySet());
        Collections.sort(keys);
        for(String key : keys) {
            sb.append(key).append(" ").append(node.getAttribute(key));
        }
        return sb.toString().strip();
    }
    public static String createLocator(NodeLocator node) {
        StringBuilder sb = new StringBuilder();
        String id = node.getAttribute(ConfigProperty.ID);
        if(!isEmpty(id)) {
            sb.append(ConfigProperty.ID).append("(").append(id).append("),");
        }
        String css = node.getAttribute(ConfigProperty.CSS_SELECTOR);
        if(!isEmpty(css)) {
            sb.append(ConfigProperty.CSS_SELECTOR).append("(").append(css).append("),");
        }

        String tag = node.getAttribute(ConfigProperty.TAG);
        if(!isEmpty(tag)) {
            if(tag.startsWith("#")) {
                sb.append(ConfigProperty.CSS_SELECTOR).append("(").append(tag).append("),");
            } else {
                sb.append(ConfigProperty.TAG).append("(").append(tag).append("),");
            }
        }
        //String text = node.getAttribute(MetaTags.TEXT);
        //if(!isEmpty(text)) {
        //    sb.append(MetaTags.TEXT).append("(").append(text).append("),");
        //}

        String str = sb.toString();
        return str.substring(0, str.length()-1);
    }
}
