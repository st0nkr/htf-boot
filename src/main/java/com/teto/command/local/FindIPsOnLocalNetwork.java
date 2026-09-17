package com.teto.command.local;

import com.teto.IDuration;
import com.teto.IIPAddresses;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.command.services.RunQuickLocalNetworkScan;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FindIPsOnLocalNetwork extends AbstractCommand<ScannedTargets> implements IIPAddresses, IDuration {
    private final Provenance prov;

    public FindIPsOnLocalNetwork(Provenance prov) {
        this.prov = prov;
    }

    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        Optional<Target> local = ctx.apply(new FindLocalSubNetTarget());
        if(isPresent(local)) {
            Target loc = local.get();
            return ctx.apply(new RunQuickLocalNetworkScan(loc));
        }
        return empty();
    }
}
