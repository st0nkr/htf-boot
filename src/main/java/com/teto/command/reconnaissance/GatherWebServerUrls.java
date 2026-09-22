package com.teto.command.reconnaissance;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.gobuster.GatherGoBusterDirectories;
import com.teto.command.katana.GatherKatanaDirectories;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.Target;

import java.util.Optional;

public class GatherWebServerUrls extends AbstractCommand<Void> {
    private final TargetNode node;
    private final Target target;

    public GatherWebServerUrls(TargetNode node, Target target) {
        this.node = node;
        this.target = target;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        ctx.apply(new GatherKatanaDirectories(node, target));
        ctx.apply(new GatherGoBusterDirectories(node, target));
        return Optional.empty();
    }
}
