package com.teto.command.regex;

import java.util.Collection;

public class CreateRegexFromSampleData extends CreateRegexPatternFromSampleData {
    public CreateRegexFromSampleData(Collection<String> sampleData) {
        super(sampleData);
    }

    public CreateRegexFromSampleData(String... sampleData) {
        super(sampleData);
    }
}
