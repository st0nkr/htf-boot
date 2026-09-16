package com.teto.command.properties;

import com.teto.IProperties;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.meta.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ApplyCliOverrides extends AbstractCommand<Void> implements IProperties {
    private final List<String> args;

    public ApplyCliOverrides(List<String> args) {
        this.args = args;
    }

    private String toKey(String arg) {
        if(!arg.contains("=")) {
            return arg.substring(1);
        }
        int idx = arg.indexOf("=");
        return arg.substring(1,idx);
    }

    private String extractValue(String arg) {
        if(!arg.contains("=")) {
            return Boolean.TRUE.toString();
        }
        String[] words = arg.split("=");
        return words[1];
    }
    @Override
    public Optional<Void> apply(Context ctx) {
        Properties props = properties(ctx);
        if(props != null) {
            for (String arg : args) {
                String key = toKey(arg);
                Tag tag = Tag.fromString(key);
                if(Tag.ConfigFileName.equals(tag)) {
                    continue;
                }
                String val = extractValue(arg);
                props.put(key, val);
            }
        }
        return Optional.empty();
    }
}
