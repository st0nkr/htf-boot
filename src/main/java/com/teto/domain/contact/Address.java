package com.teto.domain.contact;

public class Address implements Comparable<Address> {
    private final String value;

    public Address(String value) {
        this.value = value;
        assert value != null;
    }

    @Override
    public int compareTo(Address o) {
        return value.compareTo(o.getValue());
    }

    public String getValue() {
        return value;
    }
}
