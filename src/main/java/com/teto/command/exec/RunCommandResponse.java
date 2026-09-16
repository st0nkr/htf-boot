package com.teto.command.exec;


public class RunCommandResponse {
    private Integer exitCode;
    private String output;
    private String error;


    public RunCommandResponse() {

    }
    public RunCommandResponse(Integer code, String response) {
        this.exitCode = code;
        this.output = response;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
