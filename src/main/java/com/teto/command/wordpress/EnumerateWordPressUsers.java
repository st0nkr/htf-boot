package com.teto.command.wordpress;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.url.Url;

import java.util.Collection;
import java.util.Optional;

public class EnumerateWordPressUsers extends AbstractCommand<Void> {
    private final TargetNode node;
    private final Collection<Url> wordPressUrls;
    public EnumerateWordPressUsers(TargetNode node, Collection<Url> wordPressUrls) {
        this.node = node;
        this.wordPressUrls = wordPressUrls;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Optional<ScannedTargets> st = ctx.apply(new ExtractWordPressUsersFromUrls(node, wordPressUrls));
        if(isPresent(st)) {
            node.getScannedTargets().add(st.get());
        }
        st = ctx.apply(new RunWordPressEnumerateUsersScan(node, wordPressUrls));
        return Optional.empty();
    }
}
