package com.teto.domain.target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TargetTypeMapper {
    private Map<TargetType, List<String>> map = new HashMap<>();

    public TargetType targetTypeFor(String name) {
        for(TargetType targetType : map.keySet()) {
            List<String> texts = map.get(targetType);
            for(String text : texts) {
                if(name.toLowerCase().startsWith(text.toLowerCase())) {
                    return targetType;
                }
            }
        }
        return null;
    }

    public void add(TargetType tt, List<String> list) {
        if(!map.containsKey(tt)) {
            map.put(tt, list);
        } else {
            map.get(tt).addAll(list);
        }
    }
}
