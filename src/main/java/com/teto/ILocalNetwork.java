package com.teto;

import com.teto.command.Context;
import com.teto.command.local.CreateLocalNetwork;
import com.teto.command.services.DetectAllLocalHostServices;
import com.teto.command.target.SaveLocalTargets;
import com.teto.domain.local.TargetNetwork;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.Optional;

public interface ILocalNetwork extends IOptional, ITarget, ILogger{
    default Optional<TargetNetwork> getLocalNetwork(Context ctx, boolean detectServices) {
        TargetNetwork ln = ctx.fetch(TargetNetwork.class);
        if(ln == null) {
            Optional<TargetNetwork> ret = ctx.apply(new CreateLocalNetwork());
            if(isPresent(ret)) {
                ctx.apply(new SaveLocalTargets(ret.get()));
                ctx.stash(ln = ret.get());
                if(detectServices && !ln.getTargetNodes().isEmpty()) {
                    for(TargetNode lt : ln.getTargetNodes()) {
                        Target target = lt.getTarget();
                        TargetType tt = getTargetType(ctx, target);
                        if(tt != null) {
                            switch(tt) {
                                case VirtualBox -> ctx.apply(new DetectAllLocalHostServices(lt));
                            }
                        }
                    }
                }
            }
        }
        return optional(ln);
    }
}
