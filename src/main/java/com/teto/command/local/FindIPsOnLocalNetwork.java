package com.teto.command.local;

import com.teto.IDuration;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FindIPsOnLocalNetwork extends AbstractCommand<List<Target>> implements IDuration {
    private final Provenance prov;

    public FindIPsOnLocalNetwork(Provenance prov) {
        this.prov = prov;
    }

    @Override
    public Optional<List<Target>> apply(Context ctx) {
        final List<Target> targets = new ArrayList<>();
        Optional<Target> local = ctx.apply(new FindLocalSubNetTarget());
        if(isPresent(local)) {
            Target loc = local.get();
            loc.setProvenance(prov.name());
            targets.add(local.get());
            String cmd = "nmap -sn " + local.get().getSubNetMask() + " -oN -";

            Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, seconds(30)));
            if (isPresent(rsp)) {
                String[] lines = rsp.get().getOutput().split("\n");
                for (String line : lines) {
                    if (line.toLowerCase().startsWith("nmap scan report for")) {
                        String[] parts = line.strip().split(" for");
                        String ip = parts[1].strip();
                        if (!ip.endsWith(".254") && !ip.equals(local.get().getIpAddress())) {
                            Target t = new Target();
                            t.setTargetType(TargetType.LocalHost.name());
                            t.setIpAddress(ip);
                            t.setName(ip);
                            t.setUri(ip);
                            t.setProvenance(prov.name());
                            targets.add(t);
                        }
                    }
                }
            }
        }
        return optional(targets);
    }
}
