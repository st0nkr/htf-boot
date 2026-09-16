package com.teto;

import com.teto.command.Context;
import com.teto.domain.target.TargetType;

import java.util.Collection;
import java.util.Map;

public interface IVersionNumber extends ITargetBuilder {

    default String getVersionNumber(Context ctx, String text) {
        Map<TargetType, Collection<String>> targetTypes = extractTargetTypes(ctx, text);
        return null;
    }
}
