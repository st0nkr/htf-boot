package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class FingerPrint extends BaseEntity {
    public static final String FINGERPRINT_TAG = "osfingerprint";
    private String fingerPrint;

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }
}
