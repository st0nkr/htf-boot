package com.teto.command.attack;

import com.teto.IAttackVector;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.attack.AttackVector;
import com.teto.domain.local.TargetNode;

import java.util.Optional;

public class IdentifyAttackVectors extends AbstractCommand<AttackVector> implements IAttackVector {
    private final TargetNode root;

    public IdentifyAttackVectors(TargetNode root) {
        this.root = root;
    }


    @Override
    public Optional<AttackVector> apply(Context ctx) {
        info(this,"Identify Attack Vectors");
        if(hasWebServer(ctx, root)) {

        }

        return Optional.empty();
    }
}
