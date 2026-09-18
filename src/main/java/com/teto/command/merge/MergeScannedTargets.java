package com.teto.command.merge;

import com.teto.IAttackVector;
import com.teto.IMerge;
import com.teto.IScripts;
import com.teto.ITargetNode;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MergeScannedTargets extends AbstractCommand<ScannedTargets> implements ITargetNode, IScripts, IMerge, IAttackVector {
    private final TargetNode node;

    public MergeScannedTargets(TargetNode node) {
        this.node = node;
    }


    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        if (hasWebServer(ctx, node)) {
            List<Target> targets = getTargets(ctx, node, TargetType.Service, 80l);
            if(targets.size() == 2) {
                Target t1 = targets.get(0);
                Target t2 = targets.get(1);
                Target http = mergeTargets(t1, t2);
                final List<Target> replace = new ArrayList<>();
                for(Target t : node.getScannedTargets().getTargets()) {
                    if(t.equals(t1) || t.equals(t2)) {
                        continue;
                    }
                    replace.add(t);
                }
                replace.add(http);
                node.getScannedTargets().setTargets(replace);
            }
            targets = getTargets(ctx, node, TargetType.Service, 443L);
            if(targets.size() == 2) {
                Target t1 = targets.get(0);
                Target t2 = targets.get(1);
                Target https = mergeTargets(t1, t2);
                final List<Target> replace = new ArrayList<>();
                for(Target t : node.getScannedTargets().getTargets()) {
                    if(t.equals(t1) || t.equals(t2)) {
                        continue;
                    }
                    replace.add(t);
                }
                replace.add(https);
                node.getScannedTargets().setTargets(replace);
            }
        }
        if(hasSamePorts(ctx, node)) {
           info(this, "Duplicate service ports detected...merge required");
        }
        return Optional.empty();
    }

    private Target mergeTargets(Target t1, Target t2) {
        return merge(t1, t2);
    }
}
