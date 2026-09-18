package com.teto;

import com.teto.command.Context;
import com.teto.domain.bash.Bash;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ISSH {

    default SSHClient sshClient(Context ctx) {
        return ctx.fetch(SSHClient.class);
    }
    default SSHClient sshClient(Context ctx, Target target, ScannedUser user) {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        String host = target.getIpAddress();
        Integer port = convertToInt(target.getPortNumber());
        try {
            ssh.connect(host, port);
            ssh.authPassword(user.getUserName(), user.getPassword());
            ctx.stash(ssh);
            return ssh;
        } catch(Exception e) {
            return null;
        }
    }

    default String sshCommand(Context ctx, int linenumber, String cmd) {
        SSHClient client = sshClient(ctx);
        List<String> list = sshCommand(ctx, client, cmd);
        if(list.isEmpty()) {
            return null;
        }
        return list.get(linenumber);
    }

    default List<String> sshCommand(Context ctx, SSHClient ssh, String cmd) {
        try {
            Session session = ssh.startSession();
            Session.Command sc = session.exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            String line;
            final List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            sc.join(5, TimeUnit.SECONDS);
            session.close();
            return lines;
        } catch(Exception e) {
            return null;
        }
    }
    default List<String> sshCommand(Context ctx, SSHClient ssh, Bash bash) {
        try {
            Session session = ssh.startSession();
            Session.Command sc = session.exec(bash.getCommand());
            BufferedReader reader = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            String line;
            final List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            sc.join(5, TimeUnit.SECONDS);
            session.close();
            return lines;
        } catch(Exception e) {
            return null;
        }
    }

    default Integer convertToInt(Long portNumber) {
        if(portNumber == null) {
            return null;
        }
        return portNumber.intValue();
    }
}
