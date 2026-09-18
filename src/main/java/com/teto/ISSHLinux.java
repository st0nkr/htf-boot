package com.teto;

import com.teto.command.Context;
import com.teto.domain.uname.Uname;
import net.schmizz.sshj.SSHClient;

import java.util.List;

public interface ISSHLinux extends ISSH, IBash{

    default List<String> getPasswordFileContents(Context ctx, SSHClient sshClient) {
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

    default Uname sshUname(Context ctx) {
        String kernalName = sshCommand(ctx, 0, "uname -s");
        String nodeName = sshCommand(ctx, 0, "uname -n");
        String kernelRelease = sshCommand(ctx, 0, "uname -r");
        String kernelVersion = sshCommand(ctx, 0, "uname -v");
        String machineName = sshCommand(ctx, 0, "uname -m");
        String processors = sshCommand(ctx, 0, "uname -p");
        String hardwarePlatform = sshCommand(ctx, 0, "uname -i");
        String os = sshCommand(ctx, 0, "uname -o");
        Uname un = new Uname();
        un.setKernalName(kernalName);
        un.setNodeName(nodeName);
        un.setKernelRelease(kernelRelease);
        un.setKernelVersion(kernelVersion);
        un.setMachineName(machineName);
        un.setProcessors(processors);
        un.setHardwarePlatform(hardwarePlatform);
        un.setOs(os);
        return un;
    }
    default List<String> sshOSRelease(Context ctx) {
        return sshCommand(ctx, sshClient(ctx), osRelease());
    }
}
