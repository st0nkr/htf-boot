package com.teto.domain.parser;

import com.teto.domain.process.ProcessResponse;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.Target;

public class ParserRequest {
    private final Target parent;
    private ProcessResponse response;
    private boolean verify = false;
    private boolean sudo = false;
    private boolean proxyChains = false;
    private String outputFileName;
    private String outputDirectory;
    private String contents;
    private Provenance provenance;
    private Script script;
    private boolean executed = false;
    public ParserRequest(Target parent, Provenance provenance) {
        this.parent = parent;
        this.provenance = provenance;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public void setProvenance(Provenance provenance) {
        this.provenance = provenance;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Target getParent() {
        return parent;
    }

    public ProcessResponse getResponse() {
        return response;
    }

    public void setResponse(ProcessResponse response) {
        this.response = response;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public boolean isSudo() {
        return sudo;
    }

    public void setSudo(boolean sudo) {
        this.sudo = sudo;
    }

    public boolean isProxyChains() {
        return proxyChains;
    }

    public void setProxyChains(boolean proxyChains) {
        this.proxyChains = proxyChains;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Provenance getProvenance() {
        return provenance;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

}
