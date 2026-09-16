package com.teto.domain.nmap;

import java.util.ArrayList;
import java.util.List;

public class HostScript {
    private List<Script> scripts = new ArrayList<>();

    public HostScript() {

    }
    public List<Script> getScripts() {
        return scripts;
    }

    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
    }
}
