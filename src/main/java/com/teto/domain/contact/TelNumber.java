package com.teto.domain.contact;

public class TelNumber  implements Comparable<TelNumber>{
    private final String number;

    public TelNumber(String number) {
        this.number = number;
        assert number != null;
    }

    @Override
    public int compareTo(TelNumber o) {
        return number.compareTo(o.getNumber());
    }

    public String getNumber() {
        return number;
    }
}
