package com.teto.command.local;

import com.teto.ITarget;
import com.teto.IWordList;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.target.IdentifyTargetTypeByName;
import com.teto.command.wordpress.CreateWordPressUser;
import com.teto.domain.local.TargetNetwork;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.user.ScannedUser;

import java.util.*;
import java.util.stream.Collectors;

public class CreateLocalNetwork extends AbstractCommand<TargetNetwork> implements IWordList, ITarget {
    @Override
    public Optional<TargetNetwork> apply(Context ctx) {
        Optional<ScannedTargets> st = ctx.apply(new FindIPsOnLocalNetwork(Provenance.SpringBoot));
        String[] passwordFiles = property(ctx, Tag.PasswordFilesToUse).split(",");

        if(isPresent(st)) {
            Collection<Target> targets = st.get().getTargets();
            info(this,"Found "+targets.size()+" targets on local network");
            Target localMe = extractLocalMe(targets);
            if(localMe != null) {
                targets = removeIpTargets(localMe.getIpAddress(), targets);
                final TargetNetwork tn = new TargetNetwork(localMe);
                for(Target ip : targets) {
                    ctx.apply(new IdentifyTargetTypeByName(ip));
                    // Set the password files to use during cracking
                    Optional<TargetNode> tnode = ctx.apply(new CreateTargetNode(ip, passwordFiles));
                    if(isPresent(tnode)) {
                        tn.getTargetNodes().add(tnode.get());
                    }
                }
                return optional(tn);
            }
        }
        return empty();
    }

    private Collection<Target> removeIpTargets(String ipAddress, Collection<Target> targets) {
        final Collection<Target> others = new HashSet<>();
        for(Target target : targets) {
            if(!ipAddress.equals(target.getIpAddress())) {
                others.add(target);
            }
        }
        return others;
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
