package com.teto.command.local;

import com.teto.ITarget;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.target.IdentifyTargetTypeByName;
import com.teto.domain.local.TargetNetwork;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.Collection;
import java.util.Optional;

public class CreateLocalNetwork extends AbstractCommand<TargetNetwork> implements ITarget {
    @Override
    public Optional<TargetNetwork> apply(Context ctx) {
        Optional<ScannedTargets> st = ctx.apply(new FindIPsOnLocalNetwork(Provenance.SpringBoot));
        if(isPresent(st)) {
            Collection<Target> targets = st.get().getTargets();
            info(this,"Found "+targets.size()+" targets on local network");
            Target localMe = extractLocalMe(targets);
            if(localMe != null) {
                final TargetNetwork ln = new TargetNetwork(localMe);
                for(Target ip : targets) {
                    ctx.apply(new IdentifyTargetTypeByName(ip));
                    if(!ip.getIpAddress().equalsIgnoreCase(localMe.getIpAddress())) {
                        ln.getTargetNodes().add(new TargetNode(ip));
                    }
                }
                return optional(ln);
            }

        }
        return empty();
    }

    private Target extractLocalMe(Collection<Target> targets) {
        for(Target target : targets) {
            if(TargetType.LocalMe.name().equalsIgnoreCase(target.getTargetType())) {
                return target;
            }
        }
        return null;
    }
}
