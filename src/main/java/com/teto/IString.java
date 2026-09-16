package com.teto;

import com.teto.command.Context;
import com.teto.domain.word.Word;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IString extends IProperties , IList{

    default String extractTextBetween(String text, String left, String right) {
        return org.apache.commons.lang3.StringUtils.substringBetween(text, left, right);
    }
    default List<String> extractBracketedTexts(String str) {
        List<String> result = new ArrayList<>();
        boolean flg = true;
        String text = str;
        while(flg) {
            String res = org.apache.commons.lang3.StringUtils.substringBetween(text, "(", ")");
            if(res == null || res.isEmpty()) {
                break;
            }
            result.add(res.strip());
            String rep = "("+res+")";
            int idx = text.indexOf(rep);
            int len = rep.length();
            text = text.substring(idx+len).strip();
        }
        return result;
    }
    default String starOut(String str) {
        int len = str.length();
        return starOut(str,1, len-1);
    }
    default String starOut(String str, int start, int end) {
        String text = str.substring(start,end);
        StringBuilder stars = new StringBuilder();
        for(int idx = 0 ; idx < text.length();  idx++) {
            stars.append("*");
        }
        return str.replace(text,stars);
    }

    default  String toCsvString(List<String> strings) {
        String value = null;
        if (strings != null) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String str : strings) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(str);
            }
            value = sb.toString();
        }
        return value;
    }
    default String removeDeuplicateWords(Context ctx, String str) {
        if(str == null) {
            return str;
        }
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        Set<String> added = new HashSet<>();
        for(String word : words) {
            if(added.contains(word)) {
                continue;
            }
            added.add(word);
            sb.append(word).append(" ");
        }
        return sb.toString().strip();
    }
    default List<Word> toWords(String str) {
        List<String> ret = asList(StringUtils.split(str));
        List<Word> list = new ArrayList<>();
        for(String word : ret) {
            Word w = new Word(word, replacePuncs(word," "));
            list.add(w);
        }
        return list;
    }

    default String replacePuncs(String text, String rep) {
        String[] punks = new String[] {
                "."  ,";","'","`","+","?","~","<",">","^","&","&&",
                "`","\"","-","_",":",",","\t","\r","\n",
                "/","(",")","{","}","[","]","  ",
                "*","|",";","`","!","«","=","»","'",
                "?","’"

        };
        text = StringUtils.replace(text,"  ", " ");
        for(String punk : punks) {
            text = StringUtils.replace(text,punk,rep);
        }

        return text.strip();
    }


    default String removeNumbers(Context ctx, String text) {
        List<String> numbers = extractNumbers(text);
        String ret = text;
        for(String number : numbers) {
            ret = ret.replace(number," ");
        }
        return ret.replace("  "," ").strip();
    }

    default List<String> extractNumbers(String str) {
        Pattern p = Pattern.compile("\\d");
        //  get a matcher object
        Matcher m = p.matcher(str);
        List<String> sequences = new Vector<>();
        while(m.find()) {
            sequences.add(str.substring(m.start(), m.end()));
        }
        return sequences;
    }

    default boolean isBracketed(String ret) {
        Character s = ret.charAt(0);
        Character e = null;
        switch(s) {
            case '{': e = '}'; break;
            case '[':  e = ']'; break;
            case '(':  e = ')'; break;
            case '<':   e = '>'; break;
        }
        if(e == null) return false;
        char last = ret.charAt(ret.length() - 1);
        return last == e;
    }

    default String collectionToString(Collection<String> set) {
        final StringBuilder sb = new StringBuilder();

        set.forEach(word -> sb.append(word).append(" "));
        return sb.toString().replaceAll("  "," ").strip();
    }

    default List<String> splitString(String text, String separator) {
        final List<String> list = new ArrayList<>();
        String[] parts = text.split(separator);
        for(String part : parts) {
            if(!part.strip().isEmpty()) {
                list.add(separator + " " + part);
            }
        }
        return list;
    }
    default Integer countStrings(String dest, String find) {
        return StringUtils.countMatches(dest, find);
    }
    default boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    default String[] splitOnUpperCase(String str) {
        if(str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < str.length(); i++) {
            if(Character.isUpperCase(str.charAt(i))) {
                sb.append(" ").append(str.charAt(i));
            } else {
                sb.append(str.charAt(i));
            }
        }
        String ret =  sb.toString().strip();
        return ret.split(" ");
    }
    default String textBetween(String start, String end, String str) {
        int s = str.indexOf(start)+start.length();
        String sub = str.substring(s);
        int e = sub.indexOf(end);
        if(e == -1) {
            return null;
        }
        sub = sub.substring(0,e);
        return sub;
    }
    static boolean isPositiveInteger(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        // numbers with leading zeros should not be treated as numbers
        // (e.g. when comparing "01" <-> "1")
        if (str.charAt(0) == '0' && str.length() > 1) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            final char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    default String strip(String str) {
        if(str == null) {
            return str;
        }
        return str.strip();
    }

    default String removeDuplicateWords(String str, String delimiter) {
        String[] words = str.split(delimiter);
        StringBuilder sb = new StringBuilder();
        Set<String> set = new TreeSet<>();
        for(String word : words) {
            if(!set.contains(strip(word))) {
                sb.append(strip(word)).append(delimiter);
                set.add(strip(word));
            }
        }
        return removeLast(sb.toString(),delimiter);
    }

    default String asLower(String str) {
        if(isEmptyString(str)) {
            return str;
        }
        return str.toLowerCase();
    }

    default String collectCapitalLetters(String str) {
        final StringBuilder sb = new StringBuilder();
        for(Character c : str.toCharArray()) {
            if(Character.isUpperCase(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    default Collection<String> extractPatterns(String regex, String str) {

        Pattern p = Pattern.compile(regex);
        //  get a matcher object
        Matcher m = p.matcher(str);
        Collection<String> sequences = new TreeSet<>();
        while(m.find()) {
            String group = str.substring(m.start(), m.end());
            if(group.isBlank() || group.length() < 3) {
                continue;
            }
            sequences.add(str.substring(m.start(), m.end()));
        }
        return sequences;
    }

    default Collection<String> extractUpperCases(String str) {
        String REGEX = "[A-Z]+";
        return extractPatterns(REGEX,str);
    }
    default String splitCamelCase(String str) {
        StringBuilder sb = new StringBuilder();
        Collection<String> uppers = extractUpperCases(str);
        String text = str;
        if(!uppers.isEmpty()) {
            for(String upper : uppers) {
                if(upper.length() > 1) {
                    String replace = capitalize(upper.toLowerCase());
                    text = text.replace(upper, replace);
                }
            }
        }
        for(Character c : text.toCharArray()) {
            if(Character.isUpperCase(c)) {
                sb.append(" ");
            }
            sb.append(c);
        }
        return sb.toString().strip();
    }
    default String toCamelCase(String str, String delimiter) {
        String[] words = str.split(delimiter);
        final StringBuilder sb = new StringBuilder();
        for(String word : words) {
            sb.append(capitalize(word.toLowerCase()));
        }
        return sb.toString();
    }

    default boolean startWithNumeric(String str) {
        String[] numbers = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for(String number : numbers) {
            if(str.startsWith(number) || str.startsWith("."+number) || str.startsWith("-"+number)) {
                return true;
            }
        }
        return false;
    }
    default String stringFlatten(Collection<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append(' ');
        }
        return sb.toString();
    }

    default String concat(String...strs) {
        StringBuilder sb = new StringBuilder();
        for(String str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }

    default String concat(Collection<String> keys, String delimiter) {
        final StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key != null) {
                sb.append(key);
                if(delimiter != null) {
                    sb.append(delimiter);
                }
            }
        }
        return removeLast(sb.toString(), delimiter);
    }

    default String concat(Collection<String> keys) {
        final StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key != null) {
                sb.append(key);
            }
        }
        return sb.toString();
    }

    default String concat(List<String> keys, String delimiter) {
        final StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key != null) {
                sb.append(key).append(delimiter);
            }
        }
        return removeLast(sb.toString(), delimiter);
    }

    default boolean isNumeric(String str) {
        return startsWithNumbers(str);
    }
    default boolean startsWithNumbers(String str) {
        int start = 0;
        if(str.startsWith("-") || str.startsWith("+")) {
            start = 1;
        }
        String pre = str.substring(start,start+1);
        return NumberUtils.isNumber(pre);
    }

    default String asString(Collection<String> lines) {
        StringBuilder sb = new StringBuilder();
        if(lines != null) {
            for (String line : lines) {
                sb.append(line).append(",");
            }
            return removeLast(sb.toString());
        }
        return sb.toString();
    }

    default String asString(String delimiter, String... objs) {
        StringBuilder sb = new StringBuilder();
        for (String obj : objs) {
            sb.append(obj).append(delimiter);
        }
        return sb.toString();
    }


    default Integer atoi(String value, int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    default String tag(String str, String pre, String post) {
        return pre + str + post;
    }

    default String buildString(Object... strings) {
        final StringBuilder sb = new StringBuilder();
        Arrays.asList(strings).forEach(string -> {
            if (!isEmptyString(string.toString())) {
                sb.append(string);
            }
        });
        return sb.toString();
    }

    default String removeFirstAndLast(String str) {
        if(str == null || str.length() < 3) {
            return str;
        }
        return removeLast(str.substring(1));
    }
    default String removeLast(String str, String remove) {
        if (isEmptyString(str)) {
            return str;
        }
        int idx = str.lastIndexOf(remove);
        return str.substring(0, idx);
    }

    default String removeLast(String str) {
        if (isEmptyString(str)) {
            return str;
        }
        int len = str.length();
        return str.substring(0, len - 1);
    }

    default String removeLastWord(String str) {
        if (isEmptyString(str)) {
            return str;
        }
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < words.length - 1; i++) {
            sb.append(words[i]).append(" ");
        }
        return str.toString().strip();
    }

    default String stringQuoted(String str, char c) {
        return c+str+c;
    }

    default String stringQuoted(String str) {
        if (isEmptyString(str)) {
            return "\"\"";
        }
        return "\"" + str + "\"";
    }

    default String stringLower(String str) {
        if (isEmptyString(str)) {
            return str;
        }
        return str.toLowerCase();
    }

    default String[] trim(String... strs) {
        String[] ret = new String[strs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = strs[i].trim();
        }
        return ret;
    }

    default String trim(String trim) {
        return trim.trim();
    }

    default String quoted(String str) {
        return "\""+str+"\"";
    }

    default String unquote(String str) {
        if(str.startsWith("\"") && str.endsWith("\"")) {
            return unquote(str.substring(1, str.length()-1));
        }
        if(str.startsWith("'") && str.endsWith("'")) {
            return unquote(str.substring(1, str.length()-1));
        }
        return str;
    }
    default boolean isQuoted(String str) {
        String text = strip(str).replaceAll(" ","");
        if(text.startsWith("\"") && text.endsWith("\"")) {
            return true;
        }
        if(text.startsWith("'") && text.endsWith("'")) {
            return true;
        }
        return false;
    }

    default Collection<String> capitalize(Collection<String> words) {
        final Collection<String> ret = new ArrayList<>();
        words.forEach(word -> {
            ret.add(capitalize(word));
        });
        return ret;
    }
    default String[] capitalize(String... strs) {
        String[] ret = new String[strs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = capitalize(strs[i]);
        }
        return ret;
    }

    default boolean isEndBracket(Character c) {
        Set<Character> set = new TreeSet<>(Arrays.asList(new Character[] { '}',']',')','>' }));
        return set.contains(c);
    }
    default boolean isStartBracket(Character c) {
        Set<Character> set = new TreeSet<>(Arrays.asList(new Character[] { '{','[','(','<' }));
        return set.contains(c);
    }
    default boolean endsWith(String str, Collection<String> endsWiths) {
        for(String ew : endsWiths) {
            if(str.endsWith(ew)) {
                return true;
            }
        }
        return false;
    }

    default boolean startsWith(String str, Collection<String> startsWith) {
        for(String sw : startsWith) {
            if(str.startsWith(sw)) {
                return true;
            }
        }
        return false;
    }
    default String replace(String str, String pattern, String replacement) {
        if(str == null || str.isEmpty() || !str.contains(replacement)) {
            return str;
        }

        String ret = str.replace(pattern, replacement);
        if(!ret.contains(pattern)) {
            return ret;
        }
        return replace(str,pattern, replacement);
    }
    default String toLowerCase(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.toLowerCase();
    }

    default Collection<String> toLower(Collection<String> strs) {
        Collection<String> ret = new ArrayList<>();
        for(String str : strs) {
            ret.add(str.toLowerCase());
        }
        return ret;
    }
    default String toLower(String str) {
        if(str == null) {
            return str;
        }
        return str.toLowerCase();
    }
    default String[] toLower(String... strs) {
        String[] ret = new String[strs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = stringLower(strs[i]);
        }
        return ret;
    }

    default String splitQuoted(String str, String delim, String quotedSeparator) {
        if (isEmptyString(str)) {
            return null;
        }
        String[] words = split(str, delim);
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(stringQuoted(word)).append(quotedSeparator);
        }
        String ret = sb.toString().trim();
        if (ret.endsWith(delim)) {
            return removeLast(ret);
        }
        return ret;
    }

    default String replaceString(String str, Map<String, String> map) {
        String newString = newString(str);
        if (isEmptyString(newString)) {
            return null;
        }
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value == null) {
                value = "";
            }
            newString = newString.replaceAll(key, value);
        }
        return newString;
    }

    default boolean isEmptyString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        String nStr = str.trim().replace("\n", "").replace("\t", "");
        return nStr.isEmpty();
    }

    default String newString(String str) {
        if (isEmptyString(str)) {
            return null;
        }
        return str;
    }

    default String commaSeparated(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for(String item : items) {
            sb.append(item).append(",");
        }
        return removeLast(sb.toString());
    }

    default String capitalizeStrings(String...names) {
        StringBuilder sb = new StringBuilder();
        for(String name : names) {
            sb.append(capitalize(name)).append(" ");
        }
        return removeLast(sb.toString());
    }
    default String capitalize(String name) {
        return StringUtils.capitalise(name);
    }

    default String[] split(String str, String delim) {
        if (isEmptyString(str)) {
            return new String[]{str};
        }
        return str.split(delim);
    }

    default Double atod(String str) {
        try {
            return Double.parseDouble(str.strip());
        } catch (Exception e) {

        }
        return null;
    }

    default Float atof(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {

        }
        return null;
    }

    default Long atol(String str) {
        try {
            return Long.parseLong(str.strip());
        } catch (Exception e) {

        }
        return null;
    }
    default Integer atoi(String str) {
        if(str == null) {
            return null;
        }
        try {
            return Integer.parseInt(str.strip());
        } catch (Exception e) {

        }
        return null;
    }

    default Boolean atob(String str) {
        try {
            return Boolean.parseBoolean(str);
        } catch(Exception e) {
            return null;
        }
    }
    default Boolean atob(String str, Boolean def) {
        try {
           return Boolean.parseBoolean(str.strip());
        } catch(Exception e) {
            return def;
        }
    }

    default String[] extractWords(String str) {
        String words = str.replace("  "," ");
        while(words.indexOf("  ") != -1) {
            words = words.replace("  "," ");
        }
        return words.split(" ");
    }

    default String brackets(String str) {
        return "("+str+")";
    }

    default String curlyBrackets(String str) {
        return "{"+str+"}";
    }

    default int getFirstIndexOf(String search, String src) {
        for(int i = 0; i < src.length(); i++) {
            String sub = src.substring(i);
            if(sub.startsWith(search)) {
                return i;
            }
        }
        return -1;
    }

    default String toString(Object o) {
        if(o == null) {
            return null;
        }
        return o.toString();
    }
    default String[] split(String str, int idx) {
        String one = str.substring(0,idx);
        String two = str.substring(idx+1);
        return new String[] {one,two};
    }

    default String getLongest(Collection<String> list) {
        String longest = null;
        for(String str : list) {
            if(longest == null) {
                longest = str;
            } else {
                if(str.length() > longest.length()) {
                    longest = str;
                }
            }
        }
        return longest;
    }
    default String getShortest(Collection<String> list) {
        String shortest = null;
        for(String str : list) {
            if(shortest == null) {
                shortest = str;
            } else {
                if(str.length() < shortest.length()) {
                    shortest = str;
                }
            }
        }
        return shortest;
    }
}
