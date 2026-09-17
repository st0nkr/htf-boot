package com.teto.domain.cidr;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;

public class CIDR extends BaseTarget  {
    @Meta(tag = Tag.Uri)
    private String uri;

    public CIDR(String name, Long pid, int level) {
        super(name, TargetType.CIDR.name(), pid, level);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
