package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;

import java.util.Arrays;

public interface ISearchSploit extends IString {

    default String addCpe(Context ctx, String cmd, String cpe) {
        if(cmd.contains("--cpe ")) {
            return cmd;
        }
        return cmd+" --cpe "+cpe;
    }
    default String addCaseSensitive(Context ctx, String cmd, String...terms) {
        if(cmd.contains("-c ")) {
            return cmd;
        }
        return cmd+" -c "+concat(Arrays.asList(terms)," ");
    }
    default String addCaseSensitive(Context ctx, String cmd) {
        if(cmd.contains("-c ")) {
            return cmd;
        }
        return cmd+" -c ";
    }

    default String addExactMatch(Context ctx, String cmd, String...terms) {
        if(cmd.contains("-e ")) {
            return cmd;
        }
        return cmd+" -e "+concat(Arrays.asList(terms), " ");
    }
    default String addExactMatch(Context ctx, String cmd) {
        if(cmd.contains("-e ")) {
            return cmd;
        }
        return cmd+" -e ";
    }

    default String addStrictMatch(Context ctx, String cmd, String...terms) {
        if(cmd.contains("-s ")) {
            return cmd;
        }
        return cmd+" -s "+concat(Arrays.asList(terms), " ");
    }
    default String addStrictMatch(Context ctx, String cmd) {
        if(cmd.contains("-s ")) {
            return cmd;
        }
        return cmd+" -s ";
    }

    default String addExcludes(Context ctx, String cmd, String...excludes) {
        if(cmd.contains("--exclude ")) {
            return cmd;
        }
        return cmd+" --exclude=\""+concat(Arrays.asList(excludes), "\"");
    }

    default Script searchSploitCPE(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.SearchSploitNMAP.name());
        String exe = "searchsploit";
        s.setBuildCommand("searchsploit -u");
        s.setExecutable(exe);
        s.setProxyChains(false);
        s.setSudo(false);
        String cmd = "---cpe $cpe -v --disable-colour --json";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        return s;
    }
    default Script searchSploitNMAP(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.SearchSploitNMAP.name());
        String exe = "searchsploit";
        s.setBuildCommand("searchsploit -u");
        s.setExecutable(exe);
        s.setProxyChains(false);
        s.setSudo(false);
        String cmd = "--nmap $nmapFile -v --disable-colour --json";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        return s;
    }

    default Script searchSploitKeyTerms(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.SearchSploitKeyTerms.name());
        String exe = "searchsploit";
        s.setBuildCommand("searchsploit -u");
        s.setExecutable(exe);
        s.setProxyChains(false);
        s.setSudo(false);
        String cmd = "-v --disable-colour --json -s $keyTerms --exclude=\"hardware\" --exclude=\"Denial of Service\" --exclude=\"local\"";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        return s;
    }
}
