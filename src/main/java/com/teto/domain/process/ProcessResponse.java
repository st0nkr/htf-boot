package com.teto.domain.process;


import java.util.List;

public class ProcessResponse {
    private  List<String> output;
    private String fileName;

    public ProcessResponse() {
    }

    public ProcessResponse(List<String> output) {
        this.output = output;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
