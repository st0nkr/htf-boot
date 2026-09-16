package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class TcpSequence extends BaseEntity {
    private final Long index;
    private final String difficulty;
    private final String values;

    public TcpSequence(Long index, String difficulty, String values) {
        this.index = index;
        this.difficulty = difficulty;
        this.values = values;
    }

    public Long getIndex() {
        return index;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getValues() {
        return values;
    }
}
