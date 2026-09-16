package com.teto.command.nmap;

import com.teto.IFile;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.nmap.NmapRun;
import com.teto.domain.parser.nmap.NmapXmlParser;

import java.util.Optional;

public class CreateNMapScan extends AbstractCommand<NmapRun> implements IFile {
    private final String xmlFile;
    private final String provenance;

    public CreateNMapScan(String xmlFile, String prov) {
        this.xmlFile = xmlFile;
        this.provenance = prov;
    }

    @Override
    public Optional<NmapRun> apply(Context ctx) {
        Optional<String> contents = readFile(xmlFile);

        if(!isPresent(contents)) {
            return empty();
        }
        String xml = contents.get();
        NmapXmlParser parser = new NmapXmlParser();
        try {
            NmapRun n = parser.parse(ctx, xml, provenance);
            return optional(n);
        } catch (Exception e) {
            logger(this).error("Parse exception ",e);
        }
        return empty();
    }
}
