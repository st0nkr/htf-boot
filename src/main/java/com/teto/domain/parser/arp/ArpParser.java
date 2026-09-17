package com.teto.domain.parser.arp;

import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.ArrayList;
import java.util.List;

public class ArpParser {
    private final String text;

    public ArpParser(String text) {
        this.text = text;
    }

    public ScannedTargets parse() {
        ScannedTargets st = new ScannedTargets();
        final List<Target> targets = new ArrayList<>();
        String[] lines = text.split("\n");
        for(int idx = 0 ; idx < lines.length; idx++) {
            if(idx == 0) {
                continue;
            }
            String line = tidy(lines[idx]);
            String[] parts = line.split(" ");

            if(parts.length == 3) {
                Target t = new Target();
                t.setUri(parts[0]);
                t.setIpAddress(parts[0]);
                targets.add(t);
            } else {
                Target t = new Target();
                t.setUri(parts[0]);
                t.setIpAddress(parts[0]);
                t.setMacAddress(parts[2]);
                targets.add(t);
            }
        }
        st.setTargets(targets);
        return st;
    }

    private String tidy(String line) {
        String ret = line.replace("  "," ").replace("\t"," ");
        while(ret.contains("  ")) ret = ret.replace("  "," ");
        return ret;
    }
}
