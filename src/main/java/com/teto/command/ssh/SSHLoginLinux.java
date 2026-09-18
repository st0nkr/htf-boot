package com.teto.command.ssh;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.passwd.Passwd;
import com.teto.domain.passwd.PasswdItem;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.uname.Uname;
import com.teto.domain.user.ScannedUser;
import net.schmizz.sshj.SSHClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SSHLoginLinux extends AbstractCommand<Void> implements IUser, IBash, ISSHLinux, IPasswd, ITarget {
    private final TargetNode node;

    public SSHLoginLinux(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Target os = getTarget(ctx, node, TargetType.OperatingSystem);
        if(os == null) {
            return empty();
        }
        if(!isLinux(ctx, os)) {
            error(this,"You have called a linux command on a non linux target");
            return empty();
        }
        final Target target = getTarget(ctx, node, "ssh");
        if(target == null) {
            info(this,"No ssh service detected");
            return empty();
        }
        final List<ScannedUser> users = getUsersWithPasswords(ctx, node, "ssh");
        if(users == null || users.isEmpty()) {
            info(this,"No users with passwords found for ssh service");
            return empty();
        }
        try {
            for(ScannedUser user : users) {
                final SSHClient sshClient = sshClient(ctx, target, user);
                if(sshClient == null) {
                    continue;
                }
                final List<String> osRelease = sshOSRelease(ctx);

                final Uname uname = sshUname(ctx);
                alignDetails(os, uname);

                final Passwd passwd = parseUsers(ctx, getPasswordFileContents(ctx, sshClient));
                List<ScannedUser> usrs = createUsers(ctx, os, passwd);
                info(this, "Add all system users");
                node.getScannedTargets().getUsers().addAll(usrs);
                sshClient.close();
            }
        } catch(Exception e) {
            error(this,"Exception caught ", e);
        }
        return Optional.empty();
    }

    private void alignDetails(Target os, Uname uname) {
        os.setHardwarePlatform(uname.getHardwarePlatform());
        os.setKernalName(uname.getKernalName());
        os.setKernelVersion(uname.getKernelVersion());
        os.setMachineName(uname.getMachineName());
        os.setNodeName(uname.getNodeName());
        os.setOsName(uname.getOs());
        os.setProcessors(uname.getProcessors());
    }

    private List<ScannedUser> createUsers(Context ctx, Target os, Passwd passwd) {
        final List<ScannedUser> usrs = new ArrayList<>();
        for(PasswdItem item : passwd.getUsers()) {
            ScannedUser su = createScannedUser(ctx, item);

            su.setLevel(os.getLevel());
            su.setParentId(os.getId());
            su.setParentType(os.getTargetType());
            su.setProvenance(Provenance.SSHJ.name());
            su.setContext("os");
            usrs.add(su);
        }
        return usrs;
    }

    private ScannedUser createScannedUser(Context ctx, PasswdItem item) {
        ScannedUser su = new ScannedUser();
        su.setUserName(item.getUserName());
        su.setConfidence(100);
        su.setUid(item.getUid());
        su.setGid(item.getGid());
        su.setUserInfo(item.getUserInfo());
        su.setHomeDirectory(item.getHomeDirectory());
        su.setShell(item.getShell());

        return su;
    }
}
