package com.teto.command.regex;

import com.teto.IRegex;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.target.TargetType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractTargetTypes extends AbstractCommand<Map<TargetType, Collection<String>>> implements IRegex {
    private final String text;

    public ExtractTargetTypes(String text) {
        this.text = text;
    }

    private Map<TargetType, Collection<String>> extractTargets(Context ctx, String str) {
        final Map<TargetType, Collection<String>> map = new HashMap<>();
        for(TargetType tt : TargetType.values()) {

            List<Pattern> patterns = getTargetTypePatterns(ctx, tt);
            if(!patterns.isEmpty()) {
                for (Pattern pattern : patterns) {
                    Matcher matcher = pattern.matcher(str);
                    if(matcher.find()) {
                        Collection<String> matches = null;

                        while (matcher.find()) {
                            String match = matcher.group();
                            if(!match.isBlank()) {
                                matches = map.get(tt);
                                if (matches == null) {
                                    map.put(tt, matches = new TreeSet<>());
                                }
                                matches.add(match);
                            }
                        }

                    }
                }
            }
        }
        return map;
    }
    @Override
    public Optional<Map<TargetType,Collection<String>>> apply(Context ctx) {
        return optional(extractTargets(ctx, text));
    }

}
