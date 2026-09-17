package com.teto.command.searchsploit;

import com.teto.IDuration;
import com.teto.IFile;
import com.teto.IScripts;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.parser.ParserRequest;
import com.teto.domain.parser.searchsploit.searchsploit.SearchSploitParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;

import java.util.Collection;
import java.util.Optional;
import java.util.TreeSet;

public class SearchSploitNMap extends AbstractCommand<Collection<Exploit>> implements IDuration, IFile, IScripts {
    private final ParserRequest req;
    private final ScannedTargets st;

    public SearchSploitNMap(ParserRequest req, ScannedTargets st) {
        this.req = req;
        this.st = st;
    }

    @Override
    public Optional<Collection<Exploit>> apply(Context ctx) {
        Optional<Script> scp = getScript(ctx, Provenance.SearchSploitNMAP);
        if(isEmpty(scp)) {
            return empty();
        }
        Script script = scp.get();
        String cmd = script.getCommandLine();
        String nmapFile = fileName(req.getOutputFileName());
        String nmapFileNoExtension = nmapFile.replace(".xml","");
        cmd = cmd.replace("$nmapFile",req.getOutputFileName());
        String json = req.getOutputFileName().replace(nmapFile, Provenance.SearchSploitNMAP.name()+"-"+nmapFileNoExtension+".json");

        cmd = cmd.replace("$json",json);
        info(this, "Executing " + cmd);

        Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(1)));
        Collection<Exploit> exploits = new TreeSet<>();
        if(rsp.isPresent()) {
            saveFile(json, rsp.get().getOutput());
            req.setOutputFileName(json);
            script.setLastCommand(cmd);
            SearchSploitParser parser = new SearchSploitParser();
            exploits.addAll(parser.parse(ctx, req).getExploits());
            exploits.forEach(exp -> {
                exp.setTargetId(req.getParent().getId());
                exp.setProvenance( Provenance.SearchSploitNMAP.name());
            });
        }

        return ctx.apply(new IdentifyRealExploits(exploits));
    }
}
