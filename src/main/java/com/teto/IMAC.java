package com.teto;

import java.util.Random;

public interface IMAC {
    default String generateRandomMacAddress() {
        Random random = new Random();
        byte[] macBytes = new byte[6];
        random.nextBytes(macBytes);

        // Set the locally administered bit (bit 0 of the first octet) to 1
        macBytes[0] |= (byte) 0x02;

        // Set the multicast bit (bit 1 of the first octet) to 0
        macBytes[0] &= (byte) 0xFE;

        StringBuilder sb = new StringBuilder();
        for (byte b : macBytes) {
            sb.append(String.format("%02x:", b));
        }
        sb.deleteCharAt(sb.length() - 1); // Remove last colon
        return sb.toString();
    }
}
