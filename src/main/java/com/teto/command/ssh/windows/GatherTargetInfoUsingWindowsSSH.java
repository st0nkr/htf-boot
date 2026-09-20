package com.teto.command.ssh.windows;

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

public class GatherTargetInfoUsingWindowsSSH extends AbstractCommand<Void> implements IUser, IBash, ISSHLinux, IPasswd, ITarget{
    private final TargetNode node;

    public GatherTargetInfoUsingWindowsSSH(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Target os = getTarget(ctx, node, TargetType.OperatingSystem);
        if(os == null) {
            warn(this,"No windoze operating system target found");
            return empty();
        }
        if(!isWindows(ctx, os)) {
            error(this,"You have called a windows command on a non windows target");
            return empty();
        }
        final Target target = getTarget(ctx, node, "ssh");
        if(target == null) {
            info(this,"No ssh service detected");
            return empty();
        }
        error(this,"Logic needs to be implemented");
        return empty();
    }
}
