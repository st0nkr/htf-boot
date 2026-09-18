package com.teto.command.services;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.command.local.RunArpLocal;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.parser.ParserRequest;
import com.teto.domain.parser.nmap.NMapParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.io.File;
import java.util.Optional;

public class RunQuickLocalNetworkScan extends AbstractCommand<ScannedTargets>
        implements IScripts, IMAC, IDuration, IFile, ILogger, ILocalNetwork {
    private final Target target;

    public RunQuickLocalNetworkScan(Target target) {
        this.target = target;
    }

    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        Optional<ScannedTargets> st = ctx.apply(new RunArpLocal(target));
        StringBuilder ipAddresses = new StringBuilder();
        if(st.isPresent()) {
            for(Target t : st.get().getTargets()) {
                ipAddresses.append(t.getIpAddress()).append(" ");
            }
        }
        Optional<Script> script = getScript(ctx, Provenance.QuickLocalNetworkScan);
        ParserRequest pr = new ParserRequest(target, Provenance.QuickLocalNetworkScan);
        pr.setOutputFileName(createFileName(ctx, target, script.get()));
        boolean forceReScan = propertyBoolean(ctx, Tag.ForceReScan, false);
        // Create a CliMapper for script
        if(isPresent(script) && (forceReScan ||!fileExists(pr.getOutputFileName()))) {
            String cmd = script.get().getCommandLine();
            if(cmd.contains("$spoofMac")) {
                cmd = cmd.replace("$spoofMac", generateRandomMacAddress());
            }
            if(cmd.contains("$subnetMask")) {
                cmd = cmd.replace("$subnetMask", ipAddresses);
            }
            if(cmd.contains("$xml")) {
                cmd = cmd.replace("$xml", pr.getOutputFileName());
            }
            info(this,"Command -> "+cmd);
            Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(30)));
        }
        NMapParser parser = new NMapParser();
        ScannedTargets stargs = parser.parse(ctx, pr);
        return optional(stargs);
    }


}
