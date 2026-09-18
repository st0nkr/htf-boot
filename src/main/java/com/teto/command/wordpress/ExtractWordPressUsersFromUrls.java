package com.teto.command.wordpress;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.url.Url;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ExtractWordPressUsersFromUrls extends AbstractCommand<ScannedTargets> {
    private final TargetNode node;
    private final Collection<Url> wordPressUrls;

    public ExtractWordPressUsersFromUrls(TargetNode node, Collection<Url> wordPressUrls) {
        this.node = node;
        this.wordPressUrls = wordPressUrls;
    }

    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        ScannedTargets st = new ScannedTargets();
        warn(this,"This is not implemented yet");
        return optional(st);
    }
}
