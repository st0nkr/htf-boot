package com.teto.command.ssh;

import com.teto.IBash;
import com.teto.ITarget;
import com.teto.IUser;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SSHLogin extends AbstractCommand<Void> implements IUser, IBash, ITarget {
    private final TargetNode node;

    public SSHLogin(TargetNode node) {
        this.node = node;
    }

    private List<String> sshCommand(Context ctx, SSHClient ssh, Bash bash) {
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

    List<String> getPasswordFileContents(Context ctx, SSHClient sshClient) {
        List<String> exists = sshCommand(ctx, sshClient,fileExists("/etc/password"));
        String fileName = "/etc/password";
        if(exists.get(0).equals("nope")) {
            exists = sshCommand(ctx, sshClient,fileExists("/etc/passwd"));
            if(exists.get(0).equals("yep")) {
                fileName = "/etc/passwd";
            }
        }
        return sshCommand(ctx, sshClient, catFile(fileName));
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        info(this, "Decide what the target OS is");
        Target os = getTarget(ctx, node, TargetType.OperatingSystem);
        if(os != null) {
            if(isLinux(ctx, os)) {

            }
        }
        final Target sshTarget = getTarget(ctx, node, "ssh");
        if(sshTarget == null) {
            info(this,"No ssh service detected");
            return empty();
        }
        final List<ScannedUser> users = getUsersWithPasswords(ctx, node, "ssh");
        if(users == null || users.isEmpty()) {
            info(this,"No users with passwords found for ssh service");
            return empty();
        }
        try {
            final String host = sshTarget.getIpAddress();
            final Integer port = convertToInt(sshTarget.getPortNumber());
            final SSHClient sshClient = new SSHClient();
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            for(ScannedUser user : users) {

                String username = user.getUserName();
                String password = user.getPassword();
                sshClient.connect(host, port);
                sshClient.authPassword(username, password);
                final List<String> osRelease = sshCommand(ctx, sshClient, osRelease());

                final List<String> uname = sshCommand(ctx, sshClient, uname());
                final List<String> passwords = getPasswordFileContents(ctx, sshClient);
                sshClient.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    private Integer convertToInt(Long portNumber) {
        if(portNumber == null) {
            return null;
        }
        return portNumber.intValue();
    }
}
