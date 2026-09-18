package com.teto.domain.parser.gobuster;

import com.teto.ICSV;
import com.teto.IFile;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.url.Url;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoBusterParser  implements IFile, ICSV {

    public ScannedTargets parse(Context ctx, Target parent, String fileName) {
        ScannedTargets st = new ScannedTargets();
        Optional<List<String>> contents = readFileAsLines(fileName);
        if(contents.isPresent()) {
            List<Url> urls = convertToUrls(ctx, parent, contents.get());
            st.setUrls(urls);
            System.out.println(urls);
        }
        return st;
    }

    private List<Url> convertToUrls(Context ctx, Target parent, List<String> contents) {
        List<String[][]> records = toRecords(contents);
        final List<Url> urls = new ArrayList<>();
        for(String[][] record : records) {
            Url url = new Url();
            url.setName(record[0][0]);
            url.setResponseCode(Integer.parseInt(record[0][1]));
            url.setSize(Integer.parseInt(record[0][2]));
            url.setProvenance(Provenance.GoBusterDir.name());
            url.setParentId(parent.getId());
            url.setParentType(parent.getTargetType());
            url.setLevel(parent.getLevel()+1);
            if(record[0].length > 3) {
                url.setUrl(record[0][3]);
            }
            urls.add(url);
        }
        return urls;
    }

    private List<String[][]> toRecords(List<String> lines) {
        List<String> csvs = toCSVS(lines);
        List<String[][]> records = new ArrayList<>();
        for(String line : csvs) {
            records.add(loadCSVStrings(line));
        }
        return records;
    }

    private List<String> toCSVS(List<String> lines) {
        final List<String> ret = new ArrayList<>();
        for(String line : lines) {
            ret.add(toCSV(line));
        }
        return ret;
    }
    private String toCSV(String line) {
        String str = line.replace("\t"," ").replace("Status: ","").replace("(","");
        str = str.replace(")","").replace("Size: ","").replace("--> ","");
        str = str.replace("[","\"").replace("]","\"");
        while(str.contains("  ")) {
            str = str.replace("  "," ");
        }
        return str.replace(" ",",");
    }

}
