package com.teto.domain.parser.nikto;

import com.teto.*;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.url.Url;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

public class NiktoParser implements IFile, IStream, ILogger, IJSON, ITargetBuilder {
    private StringBuilder elementValue;

    public ScannedTargets parse(Context ctx, Target target, String fileName) {
        try {
            Optional<String> contents = readFile(fileName);
            if(contents.isPresent()) {
                try {
                    Optional<ArrayList> json = fromJson(contents.get(), ArrayList.class);
                    final ScannedTargets targs = new ScannedTargets();

                    for(int idx = 0 ; idx < json.get().size() ; idx++) {
                        LinkedHashMap item = (LinkedHashMap) json.get().get(idx);
                        ArrayList vulns = (ArrayList) item.get("vulnerabilities");
                        for(int vi = 0 ; vi < vulns.size(); vi++) {
                            LinkedHashMap vuln = (LinkedHashMap) vulns.get(vi);

                            Url u = createUrl(target, vuln);
                            targs.getUrls().add(u);
                        }
                    }
                    return targs;
                } catch(Exception e) {
                    return new ScannedTargets();
                }
            }
        } catch(Exception e) {
            logger(this).error("Exception caught ",e);
        }
        return new ScannedTargets();
    }

    private Url createUrl(Target t,LinkedHashMap vuln) {
        String eyeDee = (String)vuln.get("id");
        String method = (String)vuln.get("method");
        String msg = (String)vuln.get("msg");
        String url = (String)vuln.get("url");
        String refs = (String)vuln.get("references");
        Url u = new Url();
        u.setName(url);
        u.setEyeDee(eyeDee);
        u.setUrl(url);
        u.setMethod(method);
        u.setMessage(msg);
        u.setRefs(refs);
        u.setProvenance(Provenance.Nikto.name());
        u.setParentType(t.getTargetType());
        u.setLevel(t.getLevel()+1);
        return u;
    }
}
