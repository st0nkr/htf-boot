package com.teto.domain.process;


import java.io.File;

public class ProcessRequest {
    private File stdout;
    private File stderr;
    private Boolean useProxyChains = true;
    IProcessCallBack callback;
    private String command;
    private boolean sudo = true;
    private String outputFile;

    public ProcessRequest() {
    }

    public File getStdout() {
        return stdout;
    }

    public void setStdout(File stdout) {
        this.stdout = stdout;
    }

    public File getStderr() {
        return stderr;
    }

    public void setStderr(File stderr) {
        this.stderr = stderr;
    }

    public Boolean getUseProxyChains() {
        return useProxyChains;
    }

    public void setUseProxyChains(Boolean useProxyChains) {
        this.useProxyChains = useProxyChains;
    }

    public IProcessCallBack getCallback() {
        return callback;
    }

    public void setCallback(IProcessCallBack callback) {
        this.callback = callback;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isSudo() {
        return sudo;
    }

    public void setSudo(boolean sudo) {
        this.sudo = sudo;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
