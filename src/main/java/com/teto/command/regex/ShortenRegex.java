package com.teto.command.regex;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;

import java.util.Optional;

public class ShortenRegex extends AbstractCommand<String> {
    private final String regex;

    public ShortenRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public Optional<String> apply(Context ctx) {
        if (regex == null) {
            return Optional.empty();
        }
        com.teto.domain.regex.ShortenRegex shortener = new com.teto.domain.regex.ShortenRegex(regex);
        String result = shortener.getRegexPattern();
        return optional(result);
    }
}
