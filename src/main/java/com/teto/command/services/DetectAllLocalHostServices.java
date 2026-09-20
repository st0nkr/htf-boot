package com.teto.command.services;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.command.searchsploit.SearchSploitNMap;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.parser.ParserRequest;
import com.teto.domain.parser.nmap.NMapParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.Collection;
import java.util.Optional;

public class DetectAllLocalHostServices extends AbstractCommand<ScannedTargets>
        implements IScripts, IMAC, IDuration, IFile, ILogger, ILocalNetwork {
    private final TargetNode lt;

    public DetectAllLocalHostServices(TargetNode lt) {
        this.lt = lt;
    }

    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        Target target = lt.getTarget();
        final ScannedTargets scannedTargets = new ScannedTargets();
        Provenance[] provs = new Provenance[] { Provenance.DetectTCPServices, Provenance.DetectUDPServices};
        for(Provenance prov : provs) {
            Optional<Script> script = getScript(ctx, prov);
            info(this,"Detect Services for "+target.getIpAddress());
            ParserRequest pr = new ParserRequest(target, prov);
            pr.setOutputFileName(createFileName(ctx, target, script.get()));
            boolean forceReScan = propertyBoolean(ctx, Tag.ForceReScan, false);
            // Create a CliMapper for script
            if(isPresent(script) && (forceReScan ||!fileExists(pr.getOutputFileName()))) {
                String cmd = script.get().getCommandLine();
                if(cmd.contains("$spoofMac")) {
                    cmd = cmd.replace("$spoofMac", generateRandomMacAddress());
                }
                if(cmd.contains("$ip")) {
                    cmd = cmd.replace("$ip", target.getIpAddress());
                }
                if(cmd.contains("$xml")) {
                    cmd = cmd.replace("$xml", pr.getOutputFileName());
                }
                info(this,"Command -> "+cmd);
                Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(30)));
                if(isPresent(rsp)) {

                }
            }
            NMapParser parser = new NMapParser();
            ScannedTargets st = parser.parse(ctx, pr);
            st.getTargets().forEach(t -> {
                t.setIpAddress(lt.getTarget().getIpAddress());
            });
            Optional<Collection<Exploit>> exploits = ctx.apply(new SearchSploitNMap(pr, st));
            if(isPresent(exploits)) {
                st.setExploits(exploits.get());
            }
            if(lt.getScannedTargets() == null) {
                lt.setScannedTargets(st);
            } else {
                lt.getScannedTargets().add(st);
            }
            scannedTargets.add(st);
        }
        return optional(scannedTargets);
    }

}
