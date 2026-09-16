package com.teto.domain.difficulty;

public enum Difficulty {
    TrivialJoke,
    Easy,
    Medium,
    Formidable,
    WorthyChallenge,
    GoodLuck;

    public static Difficulty fromIndex(long i) {
        if(i < 3) return TrivialJoke;
        if(i < 6) return Easy;
        if(i < 11) return Medium;
        if(i < 12) return Formidable;
        if(i < 16) return WorthyChallenge;
        return GoodLuck;
    }
}
