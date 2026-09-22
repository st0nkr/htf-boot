package com.teto.command.peas;

import com.teto.IMerge;
import com.teto.IPeas;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.user.ScannedUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MergePeasInfo extends AbstractCommand<Void> implements IMerge, IPeas {
    private final TargetNode node;

    public MergePeasInfo(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        List<ScannedUser> users = getPeas(ctx, node).getUsers();
        List<ScannedUser> sshUsers = getUsers(ctx, node, "ssh");
        for(ScannedUser user : users) {
            ScannedUser sshUser = getUserByUserName(sshUsers, user.getUserName());
            if(sshUser != null) {
                ScannedUser merged = merge(user, sshUser); // This copies at leat the password
                merged.setContext("system");   // a system use is equiv to ssh user
                node.getScannedTargets().getUsers().add(merged);
            }
        }
        return Optional.empty();
    }

    private ScannedUser getUserByUserName(List<ScannedUser> users, String userName) {
        for(ScannedUser user : users) {
            if(userName.equals(user.getUserName())) {
                return user;
            }
        }
        return null;
    }

    private List<ScannedUser> getUsers(Context ctx, TargetNode node, String context) {
        final List<ScannedUser> users = new ArrayList<>();
        for(ScannedUser user : node.getScannedTargets().getUsers()) {
            if(context.equalsIgnoreCase(user.getContext())) {
                users.add(user);
            }
        }
        return users;
    }
}
