package com.teto.command.local;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.LocalNetwork;
import com.teto.domain.local.LocalTarget;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.List;
import java.util.Optional;

public class CreateLocalNetwork extends AbstractCommand<LocalNetwork> {
    @Override
    public Optional<LocalNetwork> apply(Context ctx) {
        Optional<List<Target>> ips = ctx.apply(new FindIPsOnLocalNetwork(Provenance.SpringBoot));
        if(isPresent(ips)) {
            Target localMe = extractLocalMe(ips.get());
            if(localMe != null) {
                final LocalNetwork ln = new LocalNetwork(localMe);
                for(Target ip : ips.get()) {
                    if(ip.getTargetType().equalsIgnoreCase(TargetType.LocalHost.name())) {
                        ln.getLocalTargets().add(new LocalTarget(ip));
                    }
                }
                return optional(ln);
            }

        }
        return empty();
    }

    private Target extractLocalMe(List<Target> targets) {
        for(Target target : targets) {
            if(TargetType.LocalMe.name().equalsIgnoreCase(target.getTargetType())) {
                return target;
            }
        }
        return null;
    }
}
