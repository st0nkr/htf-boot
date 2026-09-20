package com.teto.command.wordpress;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

import java.util.Optional;

public class CreateWordPressUser extends AbstractCommand<ScannedUser> {
    private final String userName;
    private final Target target;
    private final String password;

    public CreateWordPressUser(Target target, String userName, String password) {
        this.userName = userName;
        this.target = target;
        this.password = password;
    }

    @Override
    public Optional<ScannedUser> apply(Context context) {
        ScannedUser user = new ScannedUser();
        user.setFirstName(userName);
        user.setUserName(userName);
        user.setPassword(password);
        user.setProvenance(Provenance.WordPressEnumerateUsers.name());
        user.setParentId(target.getId());
        user.setContext("wordpress");
        user.setParentType(target.getTargetType());
        user.setLevel(target.getLevel()+1);
        user.setFoundBy("Luck");
        user.setConfidence(100);
        return optional(user);
    }
}
