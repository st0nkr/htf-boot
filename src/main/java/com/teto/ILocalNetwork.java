package com.teto;

import com.teto.command.Context;
import com.teto.command.local.CreateLocalNetwork;
import com.teto.command.services.DetectAllLocalHostServices;
import com.teto.command.target.SaveLocalTargets;
import com.teto.domain.local.LocalNetwork;
import com.teto.domain.local.LocalTarget;
import com.teto.domain.target.Target;

import java.util.Optional;

public interface ILocalNetwork extends IOptional{
    default Optional<LocalNetwork> getLocalNetwork(Context ctx, boolean detectServices) {
        LocalNetwork ln = ctx.fetch(LocalNetwork.class);
        if(ln == null) {
            Optional<LocalNetwork> ret = ctx.apply(new CreateLocalNetwork());
            if(isPresent(ret)) {
                ctx.apply(new SaveLocalTargets(ret.get()));
                ctx.stash(ln = ret.get());
                if(detectServices && !ln.getLocalTargets().isEmpty()) {
                    for(LocalTarget lt : ln.getLocalTargets()) {
                        ctx.apply(new DetectAllLocalHostServices(lt.getTarget()));
                    }
                }
            }
        }
        return optional(ln);
    }
}
