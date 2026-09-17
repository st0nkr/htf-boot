package com.teto.domain.parser.searchsploit.searchsploit;

import com.teto.IFile;
import com.teto.IJSON;
import com.teto.IOptional;
import com.teto.IString;
import com.teto.command.Context;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.exploit.ExploitDatum;
import com.teto.domain.exploit.ExploitRecordType;
import com.teto.domain.parser.ParserRequest;
import com.teto.domain.target.ScannedTargets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

public class SearchSploitParser implements IJSON,IString, IOptional, IFile {

    public ScannedTargets parse(String str) {
        ScannedTargets targets = new ScannedTargets();
        try {
            str = extractJSON(str);
            Optional<LinkedHashMap> json = fromJson(str, LinkedHashMap.class);
            if(json.isEmpty()){
                return targets;
            }
            ArrayList resultsExploit = (ArrayList) json.get().get("RESULTS_EXPLOIT");
            Collection<Exploit> exploits = extractExploits(resultsExploit, ExploitRecordType.exploit);
            if(exploits != null && !exploits.isEmpty()) {
                targets.getExploits().addAll(exploits);
            }
            ArrayList resultShellCode = (ArrayList) json.get().get("RESULTS_SHELLCODE");
            Collection<Exploit> shellCodes = extractExploits(resultShellCode, ExploitRecordType.shell);
            if(shellCodes != null && !shellCodes.isEmpty()) {
                targets.getExploits().addAll(shellCodes);
            }
            ArrayList resultsPaper = (ArrayList) json.get().get("RESULTS_PAPER");
            Collection<Exploit> exploitPapers = extractExploits(resultsPaper, ExploitRecordType.paper);
            if(exploitPapers != null || !exploitPapers.isEmpty()) {
                targets.getExploits().addAll(exploitPapers);
            }

        } catch(Exception e) {
        }
        return targets;
    }

    private String extractJSON(String str) {
        int idx = str.indexOf(("{"));
        if(idx != -1) {
            return str.substring(idx);
        }
        return str;
    }

    public ScannedTargets parse(Context ctx, ParserRequest req, String str) {
        ScannedTargets targets = new ScannedTargets();
        try {
            Optional<LinkedHashMap> json = fromJson(str, LinkedHashMap.class);
            if(json.isEmpty()){
                return targets;
            }
            ArrayList resultsExploit = (ArrayList) json.get().get("RESULTS_EXPLOIT");
            Collection<Exploit> exploits = extractExploits(resultsExploit, ExploitRecordType.exploit);
            if(exploits != null && !exploits.isEmpty()) {
                targets.getExploits().addAll(exploits);
            }
            ArrayList resultShellCode = (ArrayList) json.get().get("RESULTS_SHELLCODE");
            Collection<Exploit> shellCodes = extractExploits(resultShellCode, ExploitRecordType.shell);
            if(shellCodes != null && !shellCodes.isEmpty()) {
                targets.getExploits().addAll(shellCodes);
            }
            ArrayList resultsPaper = (ArrayList) json.get().get("RESULTS_PAPER");
            Collection<Exploit> exploitPapers = extractExploits(resultsPaper, ExploitRecordType.paper);
            if(exploitPapers != null || !exploitPapers.isEmpty()) {
                targets.getExploits().addAll(exploitPapers);
            }

            targets.getExploits().forEach(e -> {
                e.setTargetId(req.getParent().getId());
                e.setProvenance(req.getProvenance().name());
            });
        } catch(Exception e) {
        }
        return targets;
    }

    private String string(Object o) {
        return (String) o;
    }

    private Integer integer(Object o) {
        if (o instanceof String) {
            return atoi(string(o));
        }
        return (Integer) o;
    }

    private Boolean bool(Object o) {
        if(o instanceof String) {
            Integer i = atoi(string(o));
            if(i != null) {
                return (i == 0) ? Boolean.FALSE: Boolean.TRUE;
            }
            return atob(string(o));
        }
        if(o instanceof Integer) {
            return integer(0) != 0;
        }
        return (Boolean) o;
    }
    private Collection<Exploit> extractExploits(ArrayList list, ExploitRecordType et) {
        Collection<Exploit> exploits = new ArrayList<>();
        for(int i = 0 ; i < list.size(); i++) {
            LinkedHashMap map = (LinkedHashMap) list.get(i);
            Exploit e = new Exploit();
            e.setExploitType(et.name());
            for(Object key : map.keySet()) {
                String str = (String) key;
                ExploitDatum ed = ExploitDatum.fromString(str);
                if(str == null) {
                    System.out.println("Unknown field "+str);
                } else {

                    Object val = map.get(key);
                    switch(ed) {
                        case Title : e.setTitle(string(val)); break;
                        case Aliases: e.setAliases(string(val)); break;
                        case Application: e.setApplication(string(val)); break;
                        case Author: e.setAuthor(string(val));break;
                        case Codes: e.setCodes(string(val));break;
                        case DateAdded: e.setDate_Added(string(val));break;
                        case DatePublished: e.setDate_Published(string(val));break;
                        case DateUpdated: e.setDate_Updated(string(val));break;
                        case EDB_ID: e.setEDB_ID(string(val));break;
                        case Path: e.setPath(string(val));break;
                        case Platform: e.setPlatform(string(val));break;
                        case Verified: e.setVerified(bool(val));break;
                        case Tags: e.setTags(string(val));break;
                        case Screenshot:e.setScreenshot(string(val));break;
                        case Port: e.setPortNumber(integer(val));break;
                        case Source: e.setSource(string(val));break;
                        case Type: e.setType(string(val));break;
                        default:
                            System.out.println("Unknown field "+str);

                    }
                }
            }
            exploits.add(e);
        }
        return exploits;
    }

    public ScannedTargets parse(Context ctx, ParserRequest req) {
        if(req.getContents() != null) {
            return parse(ctx, req, req.getContents());
        }
        Optional<String> contents = readFile(req.getOutputFileName());
        if(!isPresent(contents)) {
            return null;
        }
        String str = contents.get();
        return parse(ctx, req, str);
    }


}
