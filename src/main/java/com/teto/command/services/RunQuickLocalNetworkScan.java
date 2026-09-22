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
        Optional<Script> script = getScript(ctx, Provenance.QuickLocalNetworkScan);
        ParserRequest pr = new ParserRequest(target, Provenance.QuickLocalNetworkScan);
        String fileName = createFileName(ctx, target, script.get());
        pr.setOutputFileName(fileName);
        // Create a CliMapper for script
        if(isPresent(script)) {
            Optional<RunCommandResponse> rsp = runScript(ctx, target, script.get(), sap(ctx, target, fileName));
        }
        NMapParser parser = new NMapParser();
        ScannedTargets stargs = parser.parse(ctx, pr);
        return optional(stargs);
    }

    private IScriptArgProvider sap(final Context ctx, final Target target, final String fileName) {
        return new IScriptArgProvider() {
            @Override
            public String getSpoofMAC() {
                return generateRandomMacAddress();
            }

            @Override
            public String getSubnetMask() {
                return target.getSubNetMask();
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
