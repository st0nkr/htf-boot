package com.teto.domain.port;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class ServicePort implements Comparable<ServicePort> {
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Integer parentId;
    @Meta(tag = Tag.ParentType)
    private String parentType;

    @Meta(tag = Tag.PortNumber, notnull = true)
    private Long portNumber;
    @Meta(tag = Tag.Protocol)
    private String protocol;
    @Meta(tag = Tag.IPAddress)
    private String ipAddress;
    @Meta(tag = Tag.Name)
    private String name;
    @Meta(tag = Tag.Version)
    private String version;
    @Meta(tag = Tag.Status)
    private String status;
    @Meta(tag = Tag.TimeToLive)
    private Integer ttl;
    @Meta(tag = Tag.Reason)
    private String reason;
    @Meta(tag = Tag.PortState)
    private String portState;
    @Meta(tag = Tag.Provenance)
    private String provenance;
    @Meta(tag = Tag.Level)
    private Integer level;
    @Meta(tag = Tag.Product)
    private String product;
    @Meta(tag = Tag.Score)
    private Integer score;
    @Meta(tag = Tag.Hits)
    private Integer hits;

    @Override
    public String toString() {
        return "ServicePort{" +
                "portNumber=" + portNumber +
                ", protocol='" + protocol + '\'' +
                ", name='" + name + '\'' +
                ", portState='" + portState + '\'' +
                ", product='" + product + '\'' +
                ", version='" + version + '\'' +
                ", score='" + score + '\'' +
                ", hits='" + hits + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull ServicePort o) {
        int cmp = 0;
        if(getPortNumber() < o.getPortNumber()) {
            return -1;
        }
        if(getPortNumber() > o.getPortNumber()) {
            return 1;
        }
        if(getProtocol() != null && o.getProtocol() != null) {
            cmp = getProtocol().compareTo(o.getProtocol());
            if(cmp != 0) return cmp;
        }
        if(getName() != null && o.getName() != null) {
            cmp = getName().compareTo(o.getName());
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServicePort that = (ServicePort) o;
        return Objects.equals(portNumber, that.portNumber) && Objects.equals(protocol, that.protocol) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portNumber, protocol, name);
    }
}
