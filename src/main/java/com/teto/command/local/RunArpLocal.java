package com.teto.command.local;

import com.teto.IScriptArgProvider;
import com.teto.IScripts;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.parser.arp.ArpParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.Optional;

public class RunArpLocal extends AbstractCommand<ScannedTargets> implements IScripts {
    private final Target parent;

    public RunArpLocal(Target parent) {
        this.parent = parent;
    }

    private IScriptArgProvider sap(final Context ctx) {
        return new IScriptArgProvider() {
            @Override
            public String getSpoofMAC() {
                return generateRandomMacAddress();
            }

            @Override
            public String getSubnetMask() {
                return parent.getSubNetMask();
            }

            @Override
            public String getOutputFileName() {
                return null;
            }

            @Override
            public String getUrl() {
                return toUrl(parent);
            }

            @Override
            public String getUserAgent() {
                return randomFirefox(ctx);
            }
        };
    }
    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        Optional<Script> script = getScript(ctx, Provenance.ArpNames);
        if(isPresent(script)) {
            Optional<RunCommandResponse> rsp = runScript(ctx, parent, script.get());
            ArpParser parser = new ArpParser(rsp.get().getOutput());
            return optional(parser.parse());
        }
        return Optional.empty();
    }
}
