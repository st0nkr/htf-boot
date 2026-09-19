package com.teto.domain.parser.dirsearch;

import com.teto.IFile;
import com.teto.IJSON;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.url.Url;
import com.teto.domain.user.ScannedUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;

public class DirSearchParser implements IFile, IJSON {
    public ScannedTargets parse(Context ctx, Target target, String fileName) {
        ScannedTargets st = new ScannedTargets();
        Optional<String> contents = readFile(fileName);
        if(contents.isPresent()) {
            Optional<LinkedHashMap> obj = fromJson(contents.get(), LinkedHashMap.class);
            if(obj.isPresent()) {
                LinkedHashMap lhm = obj.get();
                ArrayList results = (ArrayList) lhm.get("results");
                for(int idx = 0; idx < results.size(); idx++) {
                    LinkedHashMap item = (LinkedHashMap) results.get(idx);
                    Url url = convertToUrl(ctx, target, item);
                    if(url != null) {
                        st.getUrls().add(url);
                    }
                }
            }
        }
        return st;
    }

    private Url convertToUrl(Context ctx, Target target, LinkedHashMap lhm) {
        Url u = new Url();
        u.setParentId(target.getId());
        u.setLevel(target.getLevel()+1);
        u.setProvenance(Provenance.DirSearch.name());
        u.setParentType(target.getTargetType());
        u.setUrl((String) lhm.get("url"));
        u.setContentLength((Integer) lhm.get("content-length"));
        u.setContentType((String) lhm.get("content-type"));
        u.setRedirect((String) lhm.get("redirect"));
        u.setStatus((Integer) lhm.get("status"));
        return u;
    }
}
