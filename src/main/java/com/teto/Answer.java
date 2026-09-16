package com.teto;

public enum Answer {
    yes(1),no(0),maybe(2), unknown(-1);

    Integer ordinal;
    Answer(int ordinal) {
        this.ordinal = ordinal;
    }

    public static Answer fromOrdinal(Integer ordinal) {
        for (Answer answer : Answer.values()) {
            if (answer.ordinal.equals(ordinal)) {
                return answer;
            }
        }
        return unknown;
    }
}
