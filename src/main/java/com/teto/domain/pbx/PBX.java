package com.teto.domain.pbx;

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
public class PBX extends BaseTarget implements Comparable<PBX>{
    @Meta(tag = Tag.IPAddress)
    private String ipAddress;
    @Meta(tag = Tag.PortNumber)
    private Long portNumber;
    @Meta(tag = Tag.PortState)
    private String portState;

    public PBX(String name, Integer parentId, Integer level) {
        super(name, TargetType.PBX.name(), parentId, level);
    }


    @Override
    public int compareTo(PBX o) {
        return getIpAddress().compareTo(o.getIpAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PBX pbx = (PBX) o;
        return Objects.equals(ipAddress, pbx.ipAddress) && Objects.equals(portNumber, pbx.portNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, portNumber);
    }
}
