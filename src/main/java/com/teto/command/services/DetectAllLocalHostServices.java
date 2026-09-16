package com.teto.command.services;

import com.teto.IDuration;
import com.teto.ILogger;
import com.teto.IMAC;
import com.teto.IScripts;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.LocalTarget;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.Target;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.Optional;

public class DetectAllLocalHostServices extends AbstractCommand<List<Target>>
        implements IScripts, IMAC, IDuration, ILogger {
    private final LocalTarget localTarget;

    public DetectAllLocalHostServices(LocalTarget localTarget) {
        this.localTarget = localTarget;
    }

    @Override
    public Optional<List<Target>> apply(Context ctx) {
        Optional<Script> script = getScript(ctx, Provenance.DetectUDPServices);
        // Create a CliMapper for script
        if(isPresent(script)) {
            String cmd = script.get().getCommandLine();
            if(cmd.contains("$spoofMac")) {
                cmd = cmd.replace("$spoofMac", generateRandomMacAddress());
            }
            if(cmd.contains("$ip")) {
                cmd = cmd.replace("$ip", localTarget.getTarget().getIpAddress());
            }
            if(cmd.contains("$xml")) {
                cmd = cmd.replace("$xml", "- ");
            }
            info(this,"Command -> "+cmd);
            Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(30)));
            if(isPresent(rsp)) {
                System.out.println(rsp.get().getOutput());
            }
        }
        return Optional.empty();
    }
}
