package com.teto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface IJSON extends IStream {


    default <X> Optional<String> toJson(X x) {

        ObjectMapper mapper = getObjectMapper();

        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(x);
            return Optional.ofNullable(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    default boolean isValidJSON(String target) {
        try {
            Optional<Object> json = fromJson(target, Object.class);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    default ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        om.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        return om;
    }

    default <X> Optional<X> fromJson(String json, Class<X> clazz) {
        ObjectMapper objectMapper = getObjectMapper();

        try {
            return Optional.ofNullable(objectMapper.readValue(json, clazz));
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    default boolean isValidJson(String json) {
        Optional<Object> obj = fromJson(json, Object.class);
        if(obj.isPresent()) {
            return true;
        }
        return false;
    }

    default List<String> extractJSons(String text) {
        String regex = "\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        final List<String> groups = new ArrayList<>();
        while(matcher.find()) {
            String group = matcher.group();
            if(!group.isEmpty()) {
                if(isValidJSON(group)) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    default String extractJson(String text) {
        String regex = "\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return "{ "+matcher.group(1)+" }";
        }
        return null;
    }
    default String createJson(Object pojo) {
       return toJson(pojo).get();
    }

}
