package com.teto.domain.port;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ServicePort implements Comparable<ServicePort> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private String parentType;

    private Long portNumber;
    private String protocol;
    private String ipAddress;
    private String name;
    private String version;
    private String status;
    private Integer ttl;
    private String reason;
    private String portState;
    private String provenance;
    private Integer level;
    private String product;
    private Integer score;
    private Integer hits;
    private Integer accuracy = 100;

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
