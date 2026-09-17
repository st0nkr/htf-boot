package com.teto.domain.nameserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class NameServer extends BaseTarget implements Comparable<NameServer>{
    @Meta(tag = Tag.NameServer)
    private String nameServer;

    public NameServer(String name, Long parentId, Integer level) {
        super(name, TargetType.NameServer.name(), parentId, level);
    }

    @Override
    public int compareTo(NameServer o) {
        return getNameServer().compareTo(o.getNameServer());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NameServer that = (NameServer) o;
        return Objects.equals(nameServer, that.nameServer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nameServer);
    }
}
