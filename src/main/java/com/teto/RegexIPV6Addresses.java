package com.teto;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.regex.RegexExtractPatterns;

import java.util.*;

public class RegexIPV6Addresses extends AbstractCommand<List<String>> implements IRegexPatterns {

    private final String text;

    public RegexIPV6Addresses(String text) {
        this.text = text;
    }

    @Override
    public Optional<List<String>> apply(Context ctx) {
        Optional<List<String>> v6 = ctx.apply(new RegexExtractPatterns(ipv6, text));
        Set<String> ips = new HashSet<>();
        if(isPresent(v6)) {
            ips.addAll(v6.get());
        }
        return optional(new ArrayList<>(ips));
    }
}
