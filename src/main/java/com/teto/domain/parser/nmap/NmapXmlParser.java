package com.teto.domain.parser.nmap;

import com.teto.command.Context;
import com.teto.domain.nmap.NmapRun;
import com.teto.domain.parser.nmap.internal.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NmapXmlParser {

  public NmapRun parse(Context ctx, Path pathToXml, String prov) throws IOException, NmapParserException {
    return parse(ctx, Files.readString(pathToXml), prov);
  }

  public NmapRun parse(Context ctx, String xmlAsString, String provenance) throws NmapParserException {
    try {
      return XmlParser.parse(ctx, xmlAsString, provenance);
    } catch ( Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
