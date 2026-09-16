package com.teto.command.regex;

import com.teto.RegexIPV4Addresses;
import com.teto.RegexIPV6Addresses;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;

import java.util.*;

public class RegexCIDRNames extends AbstractCommand<List<String>>  {
    String cdr = RegexIPV4Addresses.ipv4+ "(\\/([0-9]|[1-2][0-9]|3[0-2]))";
    String cdr2 = RegexIPV6Addresses.ipv6+ "(\\/([0-9]|[1-2][0-9]|3[0-2]))";
    private final String text;

    public RegexCIDRNames(String text) {
        this.text = text;
    }

    @Override
    public Optional<List<String>> apply(Context ctx) {
        Optional<List<String>> cdrs1 = ctx.apply(new RegexExtractPatterns(cdr, text));
        Optional<List<String>> cdrs2 = ctx.apply(new RegexExtractPatterns(cdr2, text));
        Set<String> ips = new HashSet<>();
        if(isPresent(cdrs1)) {
            ips.addAll(cdrs1.get());
        }
        if(isPresent(cdrs2)) {
            ips.addAll(cdrs2.get());
        }
        return optional(new ArrayList<>(ips));
    }
}
