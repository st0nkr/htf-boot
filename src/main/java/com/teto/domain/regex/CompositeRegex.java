package com.teto.domain.regex;

import com.teto.IString;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompositeRegex implements IString {
    private final Map<String, List<RegexToken>> map = new HashMap<>();

    public CompositeRegex(String text) {
        if (text != null) {
            map.put(text, createTokens(text));
        }
    }

    public CompositeRegex(String... texts) {
        if (texts != null) {
            for (String text : texts) {
                if (text != null) {
                    map.put(text, createTokens(text));
                }
            }
        }
    }

    public CompositeRegex(Collection<String> texts) {
        if (texts != null) {
            for (String text : texts) {
                if (text != null) {
                    map.put(text, createTokens(text));
                }
            }
        }
    }

    public String getRegexPattern() {
        Set<String> list = new LinkedHashSet<>();
        for (String key : map.keySet()) {
            StringBuilder sb = new StringBuilder();
            List<RegexToken> tokens = map.get(key);
            for (RegexToken token : tokens) {
                sb.append(token.getPattern());
            }
            if (sb.length() > 0) {
                list.add(sb.toString());
            }
        }
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str).append("|");
        }
        return removeLast(sb.toString());
    }

    public Pattern getPattern() {
        return Pattern.compile(getRegexPattern());
    }

    public Boolean matches(String text) {
        if (text == null) {
            return false;
        }
        String regex = getRegexPattern();
        if (regex.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    private List<RegexToken> createTokens(String text) {
        List<RegexToken> toks = createInts(text);
        toks.addAll(createStrings(text));
        toks.addAll(createPunctuations(text));
        toks.addAll(createSpaces(text));

        Collections.sort(toks);
        return toks;
    }

    private List<RegexToken> createInts(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        List<RegexToken> toks = new ArrayList<>();
        // Extract integers
        while (matcher.find()) {
            String group = matcher.group();
            int s = matcher.start();
            int e = matcher.end();
            toks.add(new RegexToken(s,e,RegexType.NUMBER,group,"\\d+"));
        }
        return toks;
    }


    private List<RegexToken> createSpaces(String text) {
        final List<RegexToken> toks = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(text);

        // Extract integers
        while(matcher.find()) {
            String group = matcher.group();
            if (!group.isEmpty()) {
                int s = matcher.start();
                int e = matcher.end();
                RegexToken rt = new RegexToken(s,e,RegexType.SPACE,group,"\\s+");
                toks.add(rt);
            }
        }
        return toks;
    }
    private List<RegexToken> createPunctuations(String text) {
        final List<RegexToken> tokens = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\p{Punct}+");
        Matcher matcher = pattern.matcher(text);

        // Extract integers
        while(matcher.find()) {
            String group = matcher.group();
            if (!group.isEmpty()) {
                int s = matcher.start();
                int e = matcher.end();
                RegexToken rt = new RegexToken(s,e,RegexType.PUNCTUATION,group,"\\p{Punct}+");
                tokens.add(rt);
            }
        }
        return tokens;
    }

    private List<RegexToken> createStrings(String text) {
        final List<RegexToken> tokens = new ArrayList<>();

        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(text);

        // Extract integers
        while(matcher.find()) {
            String group = matcher.group();
            if (!group.isEmpty()) {
                int s = matcher.start();
                int e = matcher.end();
                RegexToken rt = new RegexToken(s,e,RegexType.STRING,group,"[a-zA-Z]+");
                tokens.add(rt);
            }
        }
        return tokens;

    }
}
