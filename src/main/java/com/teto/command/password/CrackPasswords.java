package com.teto.command.password;

import com.teto.IUser;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;

import java.util.Optional;

public class CrackPasswords extends AbstractCommand<Void> implements IUser {

    private final TargetNode node;

    public CrackPasswords(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        ctx.apply(new CrackSSHPassword(node, getUsers(ctx, node, Provenance.WordPressEnumerateUsers)));
        return Optional.empty();
    }
}
