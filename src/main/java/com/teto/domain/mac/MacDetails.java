package com.teto.domain.mac;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class MacDetails implements Serializable, Comparable<MacDetails> {
    private static final long serialVersionUID = 1L;

    private String macAddress;
    private String normalizedMac;
    private String oui;
    private String manufacturer;
    private String comment;
    private String address;
    private String country;
    private String source;
    private boolean valid;
    private boolean unicast;
    private boolean multicast;
    private boolean locallyAdministered;
    private boolean universallyAdministered;

    public MacDetails(String macAddress, String manufacturer) {
        this.macAddress = macAddress;
        this.manufacturer = manufacturer;
    }

    public MacDetails(String macAddress, String oui, String manufacturer) {
        this.macAddress = macAddress;
        this.oui = oui;
        this.manufacturer = manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MacDetails that = (MacDetails) o;
        return Objects.equals(normalizedMac, that.normalizedMac) &&
                Objects.equals(manufacturer, that.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalizedMac, manufacturer);
    }

    @Override
    public int compareTo(MacDetails o) {
        if (o == null) return 1;
        if (this.macAddress == null && o.macAddress == null) return 0;
        if (this.macAddress == null) return -1;
        if (o.macAddress == null) return 1;
        return this.macAddress.compareToIgnoreCase(o.macAddress);
    }

    @Override
    public String toString() {
        return "MacDetails{" +
                "macAddress='" + macAddress + '\'' +
                ", oui='" + oui + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", source='" + source + '\'' +
                ", valid=" + valid +
                '}';
    }
}
