package com.teto;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.regex.RegexExtractPatterns;

import java.util.*;

public class RegexIPV4Addresses extends AbstractCommand<List<String>>  {
    public static String zeroTo255
            = "(\\d{1,2}|(0|1)\\"
            + "d{2}|2[0-4]\\d|25[0-5])";
    public static final String ipv4
            = zeroTo255 + "\\."
            + zeroTo255 + "\\."
            + zeroTo255 + "\\."
            + zeroTo255;

    private final String text;

    public RegexIPV4Addresses(String text) {
        this.text = text;
    }

    @Override
    public Optional<List<String>> apply(Context ctx) {
        Optional<List<String>> v4 = ctx.apply(new RegexExtractPatterns(ipv4, text));
        Set<String> ips = new HashSet<>();
        if(isPresent(v4)) {
            ips.addAll(v4.get());
        }
        return optional(new ArrayList<>(ips));
    }
}
