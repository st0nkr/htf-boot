package com.teto.domain.waf;

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

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class WAF implements Comparable<WAF>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Meta(tag = Tag.Name)
    private String name;
    @Meta(tag = Tag.Uri)
    private String uri;
    @Meta(tag = Tag.IPAddress)
    private String ipAddress;
    @Meta(tag = Tag.PortNumber)
    private Integer portNumber;
    @Meta(tag = Tag.PayLoad)
    private String payLoad;
    @Meta(tag = Tag.ParentId)
    private Long parentId;
    @Meta(tag = Tag.ParentType)
    private String parentType;

    @Meta(tag = Tag.Provenance)
    private String provenance;
    @Meta(tag = Tag.Level)
    private Integer level;
    @Meta(tag = Tag.Confidence)
    private Double confidence;
    @Meta(tag = Tag.Type)
    private String type;

    private Boolean icmpBlocked;
    private Boolean finScanBlocked;
    private Boolean synScanBlocked;
    private Boolean packetsFiltered;
    private Boolean udpBlocked;
    private Boolean stateful;
    private Boolean allowsSpoofIpAddresses;


    @Override
    public int compareTo(WAF o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WAF waf = (WAF) o;
        return Objects.equals(name, waf.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
