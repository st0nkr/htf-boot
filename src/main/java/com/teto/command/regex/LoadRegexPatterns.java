package com.teto.command.regex;

import com.teto.IProperties;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.meta.Tag;
import org.apache.commons.collections4.properties.SortedProperties;

import java.util.Optional;

public class LoadRegexPatterns extends AbstractCommand<SortedProperties> implements IProperties {
    @Override
    public Optional<SortedProperties> apply(Context ctx) {
        SortedProperties sortedProperties = new SortedProperties();
        sortedProperties.putAll(loadProperties(ctx,property(ctx, Tag.RegexPropertiesFile)));
        return optional(sortedProperties);
    }

}
