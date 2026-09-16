package com.teto;

import com.teto.domain.meta.Tag;

public interface IMeta {

    default Tag metaString(String str) {
        return Tag.fromString(str);
    }

    default Boolean isMeta(Tag meta, String str) {
        Tag m = Tag.fromString(str);
        return meta.equals(m);
    }
}
