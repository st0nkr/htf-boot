package com.teto.domain.regex;

public class RegexToken implements Comparable<RegexToken>{
    private final int start;
    private final int end;
    private final RegexType type;
    private final String value;
    private final String pattern;

    public RegexToken(int start, int end, RegexType type, String value, String pattern) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.value = value;
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public RegexType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(RegexToken o) {
        if(start < o.start) return -1;
        if(start > o.start) return 1;
        if(end < o.end) return -1;
        if(end > o.end) return 1;
        return 0;
    }
    public Integer getLength() {
        return end - start;
    }

    @Override
    public String toString() {
        return "RegexToken{" +
                "start=" + start +
                ", end=" + end +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", pattern='" + pattern + '\'' +
                '}';
    }
}
