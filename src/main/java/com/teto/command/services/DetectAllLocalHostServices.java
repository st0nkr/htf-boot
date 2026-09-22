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
import java.util.Set;

public class DetectAllLocalHostServices extends AbstractCommand<ScannedTargets>
        implements IScripts, IMAC, IDuration, IFile, ILogger, ISet, ILocalNetwork {
    private final TargetNode targetNode;

    public DetectAllLocalHostServices(TargetNode lt) {
        this.targetNode = lt;
    }

    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        Target target = targetNode.getTarget();
        final ScannedTargets scannedTargets = new ScannedTargets();
        Provenance[] provs = new Provenance[] { Provenance.DetectTCPServices, Provenance.DetectUDPServices};
        for(Provenance prov : provs) {
            Optional<Script> script = getScript(ctx, prov);
            info(this,"Detect Services for "+target.getIpAddress());
            ParserRequest pr = new ParserRequest(target, prov);
            String fileName = createFileName(ctx, target, script.get());
            pr.setOutputFileName(fileName);
            boolean forceReScan = propertyBoolean(ctx, Tag.ForceReScan, false);
            // Create a CliMapper for script
            if(isPresent(script) && (forceReScan ||!fileExists(pr.getOutputFileName()))) {
                Optional<RunCommandResponse> rsp = runScript(ctx, target, script.get(), sap(ctx, target, fileName));
            }
            NMapParser parser = new NMapParser();
            ScannedTargets st = parser.parse(ctx, pr);
            st.getTargets().forEach(t -> {
                t.setIpAddress(targetNode.getTarget().getIpAddress());
            });
            Optional<Collection<Exploit>> exploits = ctx.apply(new SearchSploitNMap(pr, st));
            if(isPresent(exploits)) {
                st.setExploits(exploits.get());
            }
            if(targetNode.getScannedTargets() == null) {
                targetNode.setScannedTargets(st);
            } else {
                targetNode.getScannedTargets().add(st);
            }
            scannedTargets.add(st);
        }
        return optional(scannedTargets);
    }

    private IScriptArgProvider sap(final Context ctx, final Target target, final String fileName) {
        return new  IScriptArgProvider() {

            @Override
            public String getSpoofMAC() {
                return generateRandomMacAddress();
            }

            @Override
            public String getSubnetMask() {
                return "";
            }

            @Override
            public String getOutputFileName() {
                return fileName;
            }

            @Override
            public String getUrl() {
                return toUrl(target);
            }

            @Override
            public String getUserAgent() {
                return randomFirefox(ctx);
            }

            @Override
            public String getWordList() {
                return "";
            }

            @Override
            public String getUserName() {
                return "";
            }
        };
    }

}
