package com.teto.command.local;

import com.teto.ITarget;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.target.IdentifyTargetType;
import com.teto.domain.local.LocalNetwork;
import com.teto.domain.local.LocalTarget;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.Collection;
import java.util.Optional;

public class CreateLocalNetwork extends AbstractCommand<LocalNetwork> implements ITarget {
    @Override
    public Optional<LocalNetwork> apply(Context ctx) {
        Optional<ScannedTargets> st = ctx.apply(new FindIPsOnLocalNetwork(Provenance.SpringBoot));
        if(isPresent(st)) {
            Collection<Target> targets = st.get().getTargets();
            info(this,"Found "+targets.size()+" targets on local network");
            Target localMe = extractLocalMe(targets);
            if(localMe != null) {
                final LocalNetwork ln = new LocalNetwork(localMe);
                for(Target ip : targets) {
                    ctx.apply(new IdentifyTargetType(ip));
                    ln.getLocalTargets().add(new LocalTarget(ip));
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
