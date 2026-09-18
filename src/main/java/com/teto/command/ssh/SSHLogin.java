package com.teto.command.ssh;

import com.teto.IBash;
import com.teto.ITarget;
import com.teto.IUser;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.regex.ExtractTargetTypes;
import com.teto.domain.bash.Bash;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.user.ScannedUser;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SSHLogin extends AbstractCommand<Void> implements IUser, IBash, ITarget {
    private final TargetNode node;

    public SSHLogin(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        info(this, "Decide what the target OS is");
        Target os = getTarget(ctx, node, TargetType.OperatingSystem);
        if(os != null) {
            if(isLinux(ctx, os)) {
                ctx.apply(new SSHLoginLinux(node));
            }
        }
        return Optional.empty();
    }

}
