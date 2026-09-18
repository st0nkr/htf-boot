package com.teto.domain.parser.wordpress;

import com.teto.IFile;
import com.teto.IJSON;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;

public class WordPressParser implements IFile, IJSON {
    public ScannedTargets parse(Context ctx, Target target, String fileName) {
        ScannedTargets st = new ScannedTargets();
        Optional<String> contents = readFile(fileName);
        if(contents.isPresent()) {
            Optional<LinkedHashMap> obj = fromJson(contents.get(), LinkedHashMap.class);
            if(obj.isPresent()) {
                LinkedHashMap lhm = obj.get();
                LinkedHashMap users = (LinkedHashMap) lhm.get("users");
                if(users != null) {
                    Iterator iter = users.keySet().iterator();
                    while(iter.hasNext()) {
                        String userName = (String) iter.next();

                        ScannedUser user = new ScannedUser();
                        user.setFirstName(userName);
                        user.setUserName(userName);
                        user.setProvenance(Provenance.WordPressEnumerateUsers.name());
                        user.setParentId(target.getId());
                        user.setParentType(target.getTargetType());
                        user.setLevel(target.getLevel()+1);
                        LinkedHashMap details = (LinkedHashMap) users.get(userName);
                        user.setFoundBy((String) details.get("found_by"));
                        user.setConfidence((Integer) details.get("confidence"));
                        st.getUsers().add(user);
                    }
                }
            }
        }
        return st;
    }
}
