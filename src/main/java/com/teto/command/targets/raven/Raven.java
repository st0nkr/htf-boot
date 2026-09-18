package com.teto.command.targets.raven;

import com.teto.ILocalNetwork;
import com.teto.IScripts;
import com.teto.ITargetNode;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.attack.IdentifyAttackVectors;
import com.teto.command.reconnaissance.Reconnaissance;
import com.teto.domain.attack.AttackVector;
import com.teto.domain.local.TargetNetwork;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.TargetType;

import java.util.Optional;

public class Raven extends AbstractCommand<Void> implements IScripts, ILocalNetwork, ITargetNode {

    @Override
    public Optional<Void> apply(Context ctx) {
        info(this,"Cracking Raven....");
        final Optional<TargetNetwork> tn = getLocalNetwork(ctx, true);
        final TargetNetwork targetNetwork = tn.get();

        for(TargetNode node : targetNetwork.getTargetNodes()) {
            info(this, "Node Type: " + node.getTarget().getTargetType());
            if(isAVirtualBox(ctx, node)) {
                ctx.apply(new Reconnaissance(node));
                ctx.apply(new IdentifyAttackVectors(node));
            }
        }

        // Decide which target is to be attacked;
        return Optional.empty();
    }

    private TargetType targetType(TargetNode local) {
        String tt = local.getTarget().getTargetType();
        return TargetType.fromString(tt);
    }
}
