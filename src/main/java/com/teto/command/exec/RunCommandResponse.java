package com.teto.command.exec;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RunCommandResponse {
    private Integer exitCode;
    private String output;
    private String error;
    private String outputFileName;

    public RunCommandResponse() {

    }
    public RunCommandResponse(Integer code, String response) {
        this.exitCode = code;
        this.output = response;
    }

}
