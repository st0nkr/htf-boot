package com.teto.command.regex;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.regex.CompositeRegex;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CreateRegexPatternFromSampleData extends AbstractCommand<String> {
    private final Collection<String> sampleData;

    public CreateRegexPatternFromSampleData(Collection<String> sampleData) {
        this.sampleData = sampleData;
    }

    public CreateRegexPatternFromSampleData(String... sampleData) {
        this.sampleData = sampleData != null ? List.of(sampleData) : List.of();
    }

    @Override
    public Optional<String> apply(Context ctx) {
        if (sampleData == null || sampleData.isEmpty()) {
            return Optional.empty();
        }
        CompositeRegex compositeRegex = new CompositeRegex(sampleData);
        String pattern = compositeRegex.getRegexPattern();
        if (pattern == null || pattern.isEmpty()) {
            return Optional.empty();
        }
        return optional(pattern);
    }
}
