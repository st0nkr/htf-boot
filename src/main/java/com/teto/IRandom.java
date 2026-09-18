package com.teto;

import java.util.Random;

public interface IRandom {

    default Integer randomInt(int low, int high) {
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }
}
