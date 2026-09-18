package com.teto.command.reconnaissance;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.gobuster.GatherGoBusterDirectories;
import com.teto.command.merge.MergeScannedTargets;
import com.teto.command.wordpress.EnumerateWordPressUsers;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.url.Url;

import java.util.Collection;
import java.util.Optional;


public class Reconnaissance extends AbstractCommand<Void> implements ITargetNode, IScripts, IUrl, IMerge, IAttackVector {
    private final TargetNode node;

    public Reconnaissance(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        info(this, "Reconnoitering node " + node.getTarget().getName());
        ctx.apply(new MergeScannedTargets(node));
        if (hasWebServer(ctx, node)) {
            ctx.apply(new GatherGoBusterDirectories(node));
            Optional<Collection<Url>> wordPressUrls = getMatchingUrls(ctx, node, Provenance.GoBusterDir, "wordpress","wordpress/");
            wordPressUrls.ifPresent(urls -> ctx.apply(new EnumerateWordPressUsers(node, urls)));
        }
        return Optional.empty();
    }
}


