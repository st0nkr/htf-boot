package com.teto.command.targets.raven;

import com.teto.ILocalNetwork;
import com.teto.IScripts;
import com.teto.ITargetNode;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.attack.IdentifyAttackVectors;
import com.teto.command.password.CrackPasswords;
import com.teto.command.peas.MergePeasInfo;
import com.teto.command.reconnaissance.Reconnaissance;
import com.teto.command.ssh.GatherTargetInfoUsingSSH;
import com.teto.domain.local.TargetNetwork;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.TargetType;

import java.util.Arrays;
import java.util.Optional;

public class Raven extends AbstractCommand<Void> implements IScripts, ILocalNetwork, ITargetNode {

    @Override
    public Optional<Void> apply(Context ctx) {
        info(this,"Cracking Raven....");
        final Optional<TargetNetwork> tn = getLocalNetwork(ctx, true);
        final TargetNetwork targetNetwork = tn.get();

        boolean found = false;
        for(TargetNode node : targetNetwork.getTargetNodes()) {
            info(this, "Node Type: " + node.getTarget().getTargetType());
            if(isAVirtualBox(ctx, node) && !isMe(ctx, node)) {

                found = true;
                ctx.apply(new Reconnaissance(node));
                ctx.apply(new CrackPasswords(node));
                ctx.apply(new GatherTargetInfoUsingSSH(node));
                ctx.apply(new MergePeasInfo(node));
                ctx.apply(new IdentifyAttackVectors(node));
            }
        }
        if(!found) {
            warn(this, "Did not find any Raven Virtual boxes .... :(");
        }

        // Decide which target is to be attacked;
        return Optional.empty();
    }

    private TargetType targetType(TargetNode local) {
        String tt = local.getTarget().getTargetType();
        return TargetType.fromString(tt);
    }
}
