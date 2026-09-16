package com.teto.domain.service;

import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Service implements Comparable<Service> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ipAddress;
    private String protocol;
    private Long port;
    private String state;
    @Column(columnDefinition = "TEXT")
    private String cpe;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String extraInfo;
    @Column(columnDefinition = "TEXT")
    private String product;


    @Override
    public int compareTo(Service o) {
        int cmp = getIpAddress().compareTo(o.getIpAddress());
        if(cmp != 0) return cmp;
        if(getPort() != null && o.getPort() != null) {
            if(getPort() < o.getPort()) return -1;
            if(getPort() > o.getPort()) return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(ipAddress, service.ipAddress) && Objects.equals(protocol, service.protocol) && Objects.equals(port, service.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, protocol, port);
    }

    @Override
    public String toString() {
        return "Service{" +
                "ipAddress='" + ipAddress + '\'' +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                '}';
    }
}
