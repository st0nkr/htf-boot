package com.teto.command.nmap;

import com.teto.IFile;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.nmap.NmapRun;
import com.teto.domain.parser.nmap.NmapXmlParser;

import java.util.Optional;

public class CreateNMapScanFromXML extends AbstractCommand<NmapRun> implements IFile {
    private final String xml;
    private final String provenance;

    public CreateNMapScanFromXML(String xml, String prov) {
        this.xml = xml;
        this.provenance = prov;
    }

    @Override
    public Optional<NmapRun> apply(Context ctx) {

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
