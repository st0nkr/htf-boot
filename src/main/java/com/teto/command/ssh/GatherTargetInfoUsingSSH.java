package com.teto.command.ssh;

import com.teto.IBash;
import com.teto.ITarget;
import com.teto.IUser;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.peas.DetermineAttackVectorsFromPeas;
import com.teto.command.ssh.linux.GatherTargetInfoUsingLinuxSSH;
import com.teto.command.ssh.windows.GatherTargetInfoUsingWindowsSSH;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.*;

public class GatherTargetInfoUsingSSH extends AbstractCommand<Void> implements IUser, IBash, ITarget {
    private final TargetNode node;

    public GatherTargetInfoUsingSSH(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        info(this, "Decide what the target OS is");
        Target os = getTarget(ctx, node, TargetType.OperatingSystem);
        if(os != null) {
            if(isLinux(ctx, os)) {
                info(this, "Target is linux based");
                ctx.apply(new GatherTargetInfoUsingLinuxSSH(node));
                info(this, "Inspect linpeas for further vulnerabilities");
                ctx.apply(new DetermineAttackVectorsFromPeas(node));
            }
            if(isWindows(ctx,os)) {
                info(this,"Target is windoze based");
                ctx.apply(new GatherTargetInfoUsingWindowsSSH(node));
                ctx.apply(new DetermineAttackVectorsFromPeas(node));
            }
        }
        return Optional.empty();
    }



}
