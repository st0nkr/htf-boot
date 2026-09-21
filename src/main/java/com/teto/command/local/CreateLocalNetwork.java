package com.teto.command.local;

import com.teto.ITarget;
import com.teto.IWordList;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.target.IdentifyTargetTypeByName;
import com.teto.command.wordpress.CreateWordPressUser;
import com.teto.domain.local.TargetNetwork;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.user.ScannedUser;

import java.util.*;
import java.util.stream.Collectors;

public class CreateLocalNetwork extends AbstractCommand<TargetNetwork> implements IWordList, ITarget {
    @Override
    public Optional<TargetNetwork> apply(Context ctx) {
        Optional<ScannedTargets> st = ctx.apply(new FindIPsOnLocalNetwork(Provenance.SpringBoot));
        String[] passwordFiles = property(ctx, Tag.PasswordFilesToUse).split(",");

        if(isPresent(st)) {
            Collection<Target> targets = st.get().getTargets();
            info(this,"Found "+targets.size()+" targets on local network");
            Target localMe = extractLocalMe(targets);
            if(localMe != null) {
                final TargetNetwork tn = new TargetNetwork(localMe);
                for(Target ip : targets) {
                    if(!ip.getTargetType().equals(TargetType.LocalMe.name())) {
                        ctx.apply(new IdentifyTargetTypeByName(ip));
                        if (!ip.getIpAddress().equalsIgnoreCase(localMe.getIpAddress())) {
                            // Set the password files to use during cracking
                            TargetNode tnode = new TargetNode(ip);
                            tnode.setWordList(ROCK_YOU);
                            tnode.setPasswordFiles(Arrays.asList(passwordFiles));
                            List<ScannedUser> wpUsers = createWordPressUsers(ctx, ip);
                            tnode.setWordpressUsers(wpUsers);
                            tn.getTargetNodes().add(tnode);
                        }
                    }
                }
                return optional(tn);
            }

        }
        return empty();
    }

    private List<ScannedUser> createWordPressUsers(Context ctx, Target target) {
        String users = property(ctx, Tag.CreateWordPressUsers);
        String[] unames = users.split(",");
        final List<ScannedUser> usrs = new ArrayList<>();
        if(!users.isEmpty()) {
            for (String uname : unames) {
                String[] parts = uname.split(":");
                String userName = parts[0];
                String password = (parts.length == 1) ? "" : parts[1];
                Optional<ScannedUser> user = ctx.apply(new CreateWordPressUser(target, userName, password));
                usrs.add(user.get());
            }
        }
        return usrs;
    }

    private Target extractLocalMe(Collection<Target> targets) {
        for(Target target : targets) {
            if(TargetType.LocalMe.name().equalsIgnoreCase(target.getTargetType())) {
                return target;
            }
        }
        return null;
    }
}
