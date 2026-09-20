package com.teto.command.password;

import com.teto.IPort;
import com.teto.ITarget;
import com.teto.ITargetNode;
import com.teto.IUser;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.user.ScannedUser;

import java.util.Collection;
import java.util.Optional;

public class CrackPasswords extends AbstractCommand<Void> implements IUser, ITarget, IPort,ITargetNode {

    private final TargetNode node;

    public CrackPasswords(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        for(String passwordFile : node.getPasswordFiles()) {
            Long sshPort = getPort(ctx, node, "ssh");
            if(sshPort != null) {
                Optional<Collection<ScannedUser>> crackedUsers = ctx.apply(new CrackSSHPassword(node,
                        getUsers(ctx, node, Provenance.WordPressEnumerateUsers), passwordFile));
                if (isPresent(crackedUsers)) {
                    node.getScannedTargets().getUsers().addAll(crackedUsers.get());
                }
            }
        }
        return Optional.empty();
    }
}
