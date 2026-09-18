package com.teto;

import com.teto.domain.bash.Bash;

public interface IBash {

    default Bash fileExists(String fileName) {
        String cmd = "test -f " + fileName + " && echo \"yep\" || echo \"nope\"";
        return new Bash(cmd, false,"yep","nope");
    }
    default Bash uname() {
        String cmd = "uname -a";
        return new Bash(cmd, false);
    }

    default Bash osRelease() {
        String cmd = "cat /etc/os-release";
        return new Bash(cmd, true);
    }

    default Bash catFile(String fileName) {
        return new Bash("cat " + fileName, true);
    }
}
