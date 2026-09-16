package com.teto;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public interface IDuration {

    default Duration seconds(int secs) {
        return Duration.of(secs, ChronoUnit.SECONDS);
    }
    default Duration minutes(int mins) {
        return Duration.of(mins, ChronoUnit.MINUTES);
    }

    default Duration milliseconds(int ms) {
        return Duration.of(ms, ChronoUnit.MILLIS);
    }

    default Duration hours(int hours) {
        return Duration.of(hours, ChronoUnit.HOURS);
    }

    default Duration days(int days) {
        return Duration.of(days, ChronoUnit.DAYS);
    }
    default float duration(Float durn) {
        if(durn == null) {
            return 0F;
        }
        return durn;
    }
}
