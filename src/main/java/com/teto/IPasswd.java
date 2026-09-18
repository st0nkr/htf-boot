package com.teto;

import com.teto.command.Context;
import com.teto.domain.passwd.Passwd;
import com.teto.domain.passwd.PasswdItem;

import java.util.List;

public interface IPasswd {
    default Passwd parseUsers(Context ctx, List<String> lines) {
        Passwd passwd = new Passwd();
        for(String line : lines) {
            String[] parts = line.split(":");
            PasswdItem p = new PasswdItem();
            p.setUserName(parts[0]);
            // cant decide password. Keep null so it qualifies for future brute forcing
            //p.setPassword(parts[1]);
            p.setUid(Integer.parseInt(parts[2]));
            p.setGid(Integer.parseInt(parts[3]));
            p.setUserInfo(parts[4]);
            p.setHomeDirectory(parts[5]);
            p.setShell(parts[6]);
            passwd.add(p);
        }
        return passwd;
    }
}
