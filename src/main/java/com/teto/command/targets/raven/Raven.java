package com.teto.command.targets.raven;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.attack.IdentifyAttackVectors;
import com.teto.domain.attack.AttackVector;
import com.teto.domain.local.LocalNetwork;
import com.teto.domain.local.LocalTarget;
import com.teto.domain.target.TargetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Raven extends AbstractCommand<Void> {
    private final LocalNetwork localNetwork;

    public Raven(LocalNetwork localNetwork) {
        this.localNetwork = localNetwork;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        for(LocalTarget local : localNetwork.getLocalTargets()) {
            TargetType tt = targetType(local);
            switch(tt) {
                case VirtualBox -> ctx.apply(new IdentifyAttackVectors(new AttackVector(local.getTarget(), local.getScannedTargets())));
            }
        }

        // Decide which target is to be attacked;
        return Optional.empty();
    }

    private TargetType targetType(LocalTarget local) {
        String tt = local.getTarget().getTargetType();
        return TargetType.fromString(tt);
    }
}
