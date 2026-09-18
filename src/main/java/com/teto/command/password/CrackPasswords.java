package com.teto.command.password;

import com.teto.IUser;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.user.ScannedUser;

import java.util.Collection;
import java.util.Optional;

public class CrackPasswords extends AbstractCommand<Void> implements IUser {

    private final TargetNode node;

    public CrackPasswords(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Optional<Collection<ScannedUser>> crackedUsers = ctx.apply(new CrackSSHPassword(node, getUsers(ctx, node, Provenance.WordPressEnumerateUsers)));
        if(isPresent(crackedUsers)) {
            node.getScannedTargets().setUsers(crackedUsers.get());
        }
        return Optional.empty();
    }
}
