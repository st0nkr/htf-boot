package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.user.ScannedUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public interface IUser {

    default List<ScannedUser> getUsers(Context ctx, TargetNode node, Provenance prov) {
        Collection<ScannedUser> users = new TreeSet<>();
        for(ScannedUser user : node.getScannedTargets().getUsers()) {
            if(prov.name().equalsIgnoreCase(user.getProvenance())) {
                users.add(user);
            }
        }
        return new ArrayList<>(users);
    }
    default List<ScannedUser> getUsersWithPasswords(Context ctx, TargetNode node, String context) {
        Collection<ScannedUser> users = new TreeSet<>();
        for(ScannedUser user : node.getScannedTargets().getUsers()) {
            if(context.equalsIgnoreCase(user.getContext())) {
                if(user.getPassword() != null) {
                    users.add(user);
                }
            }
        }
        return new ArrayList<>(users);
    }
}
