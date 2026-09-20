package com.teto.command.peas;

import com.teto.ITarget;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.Optional;

public class DetermineAttackVectorsFromPeas extends AbstractCommand<Void> implements ITarget {
    private final TargetNode node;

    public DetermineAttackVectorsFromPeas(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Target os = getTarget(ctx, node, TargetType.OperatingSystem);
        if(isLinux(ctx, os)) {
            ctx.apply(new DetermineLinuxAttackVectorsFromPeas(node));
        }
        if(isWindows(ctx,os)) {
            ctx.apply(new DetermineWindowsAttackVectorsFromPeas(node));
        }
        return Optional.empty();
    }
}
