package com.teto.domain.parser.hydra;

import com.teto.IFile;
import com.teto.IJSON;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

public class HydraParser implements IFile , IJSON {
    public ScannedUser parse(Context ctx, Target target, String fileName) {
        Optional<String> contents = readFile(fileName);
        if(contents.isPresent()) {
            Optional<LinkedHashMap> lhm = fromJson(contents.get(), LinkedHashMap.class);
            if(lhm.isPresent()) {
                ArrayList results = (ArrayList) lhm.get().get("results");
                for(int idx = 0 ; idx < results.size(); idx++) {
                    LinkedHashMap obj = (LinkedHashMap) results.get(idx);
                    if(obj != null) {
                        String login = (String) obj.get("login");
                        String password = (String) obj.get("password");
                        ScannedUser user = new ScannedUser();
                        user.setUserName(login);
                        user.setPassword(password);
                        user.setContext("ssh");
                        user.setProvenance(Provenance.HydraSSH.name());
                        user.setParentType(target.getTargetType());
                        user.setParentId(target.getId());
                        user.setLevel(target.getLevel() + 1);
                        user.setConfidence(100);
                        return user;
                    }
                }
            }
        }
        return null;
    }
}
