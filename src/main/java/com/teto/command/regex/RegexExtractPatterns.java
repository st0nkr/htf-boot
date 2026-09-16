package com.teto.command.regex;


import com.teto.command.AbstractCommand;
import com.teto.command.Context;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExtractPatterns extends AbstractCommand<List<String>> {
    private final String regexPattern;
    private final String text;

    public RegexExtractPatterns(String regexPattern, String text) {
        this.regexPattern = regexPattern;
        this.text = text;
    }

    @Override
    public Optional<List<String>> apply(Context ctx) {
        final Set<String> list = new HashSet<>();
        try {
            Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String grp = matcher.group();
                list.add(grp);
            }
        } catch(Exception e) {
            return null;
        }
        return optional(new ArrayList<>(list));
    }
}
