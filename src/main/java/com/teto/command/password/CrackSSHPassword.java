package com.teto.command.password;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.hydra.HydraParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CrackSSHPassword extends AbstractCommand<Collection<ScannedUser>> implements IPort, IMerge, IScripts, IScriptArgProvide, IWordList {
    private final TargetNode node;
    private final List<ScannedUser> users;

    public CrackSSHPassword(TargetNode node, List<ScannedUser> users) {
        this.node = node;
        this.users = users;
    }


    @Override
    public Optional<Collection<ScannedUser>> apply(Context ctx) {
        Optional<Script> scp = getScript(ctx, Provenance.HydraSSH);
        final List<ScannedUser> scannedUsers = new ArrayList<>();
        if(isPresent(scp)) {
            final Script script = scp.get();
            final Target target = node.getTarget();
            for(ScannedUser user : users) {
                IScriptArgProvider sap = sap(ctx, node, script, user, ROCK_YOU);
                Optional<RunCommandResponse> rsp = runScript(ctx, target, script, sap(ctx, node, script, user, ROCK_YOU));
                if(fileExists(sap.getOutputFileName())) {
                    HydraParser parser = new HydraParser();
                    ScannedUser scannedUser = parser.parse(ctx, node.getTarget(), sap.getOutputFileName());
                    if(scannedUser != null) {
                        info(this,"We need to add user per context as passwords could be different");
                        ScannedUser merged = merge(scannedUser, user);
                        scannedUsers.add(merged);
                    }
                    scannedUsers.add(user);
                }
            }
        }
        return optional(scannedUsers);
    }
}
