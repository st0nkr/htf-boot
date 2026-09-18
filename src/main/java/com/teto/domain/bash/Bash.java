package com.teto.domain.bash;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Bash {
    private String command;
    private boolean responseIsMultiLined = true;
    private String[] responses;

    public Bash(String cms, boolean multi, String...rsp) {
        this.command = cms;
        this.responseIsMultiLined = multi;
        this.responses = rsp;
    }
}
