package com.teto.command.local;

import com.teto.IProperties;
import com.teto.IWordList;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.wordpress.CreateWordPressUser;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CreateTargetNode extends AbstractCommand<TargetNode> implements IProperties,IWordList {
    private final Target ip;
    private final String[] passwordFiles;

    public CreateTargetNode(Target ip, String[] passwordFiles) {
        this.ip = ip;
        this.passwordFiles = passwordFiles;
    }

    @Override
    public Optional<TargetNode> apply(Context ctx) {
        TargetNode tnode = new TargetNode(ip);
        tnode.setWordList(ROCK_YOU);
        tnode.setPasswordFiles(Arrays.asList(passwordFiles));
        List<ScannedUser> wpUsers = createWordPressUsers(ctx, ip);
        tnode.setWordpressUsers(wpUsers);
        return optional(tnode);
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
}
