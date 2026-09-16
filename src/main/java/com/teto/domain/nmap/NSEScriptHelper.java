package com.teto.domain.nmap;

import com.teto.IJSoup;
import com.teto.IRegex;
import com.teto.IString;
import com.teto.command.Context;
import com.teto.command.product.IdentifyProduct;
import com.teto.domain.cve.CVE;
import com.teto.domain.cve.CVEType;
import com.teto.domain.meta.Tag;
import com.teto.domain.nvp.NVP;
import com.teto.domain.port.ServicePort;
import com.teto.domain.product.Product;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.TargetType;
import com.teto.domain.waf.WAF;
import org.jsoup.nodes.Document;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NSEScriptHelper implements IString, IRegex , IJSoup {

    private Map<CVEType, List<NVP>> cveMap(List<String> lines) {
        Map<CVEType, List<NVP>> map = new HashMap<>();
        for(CVEType cveType : CVEType.values()) {
            map.put(cveType, extractLines(cveType, lines));
        }

        return map;
    }

    private int getEndIndex(CVEType tag, List<String> lines) {
        int idx = getStartIndex(tag.getTag(), lines);
        if(idx < 0) {
            return idx;
        }
        for(; idx < lines.size();idx++) {
            String line = lines.get(idx).strip();
            if(!line.startsWith("[")) {
                return idx - 1;
            }
        }
        return idx;
    }
    private int getStartIndex(String tag, List<String> lines) {
        int idx;
        for(idx = 0 ; idx < lines.size();idx++) {
            if(lines.get(idx).startsWith(tag)) {
                return idx+1;
            }
        }
        return -1;
    }
    private List<NVP> extractLines(CVEType type, List<String> lines) {
        final List<NVP> nvps = new ArrayList<>();
        int start = getStartIndex(type.getTag(), lines);
        if(start < 0) {
            return nvps;
        }
        int end = getEndIndex(type, lines);
        if(end < 0) {
            return nvps;
        }
        for(int idx = start; idx < end; idx++) {
            String line = lines.get(idx);
            String id = extractTextBetween(line,"[","]");
            String value = line.replace("["+id+"]","").strip();
            NVP nvp = new NVP(id,value);
            nvps.add(nvp);
        }
        return nvps;
    }

    private List<String> toLines(String str) {
        String[] lines = str.split("\n");
        final List<String> list = new ArrayList<>();
        for(String line : lines) {
            line = line.strip();
            if(line.isEmpty()) {
                continue;
            }
            list.add(line);
        }
        return list;
    }
    public List<CVE> parseCVES(Context ctx, long portId, String output) {
        Map<CVEType, List<NVP>> map = cveMap(toLines(output));
        List<CVE> cves = new ArrayList<>();
        for(CVEType ct : map.keySet()) {
            List<NVP> nvps = map.get(ct);
            for(NVP nvp : nvps) {
                CVE cve = new CVE();
                cve.setCveId(nvp.getName());
                cve.setDescription(nvp.getValue());
                //String keyWords = asString(toKeyWords(ctx, nvp.getValue()));
                //cve.setTagWords(keyWords);
                cve.setCveType(ct.name());
                cves.add(cve);
            }
        }
        return cves;
    }

    public List<Product> parseTechs(Context ctx, Long portId, String output) {

        if(output == null || output.isEmpty()) {
            return null;
        }
        final List<Product> products = new ArrayList<>();
        Document doc = extractHtml(output);
        if(doc != null) {
            String title = doc.title();
            if(title == null || title.isEmpty()) {
                return null;
            }
            Optional<Product> prod = ctx.apply(new IdentifyProduct(title, portId,  Provenance.NMap));
            // Its a service in NMap context
            Product product = prod.get();
            product.setTargetType(TargetType.Service);
            product.setPortNumber(portId);
            products.add(product);
            return products;
        }

        return products;
    }

    private String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch(Exception e) {
            return value;
        }
    }
    public Integer parseMtu(Context ctx, Long portId, String output) {
        String text = decode(output);
        String[] parts = text.split("==");
        if(parts.length == 2) {
            Integer mtu = Integer.parseInt(parts[1].strip());
            return mtu;
        }
        return -1;
    }
    public List<WAF> parseWaf(Context ctx, Long portId, String output) {
        List<WAF> wafs = new ArrayList<>();
        String[] lines = output.split("\n");
        // We only know what payload was used
        String[] parts = lines[1].split("/");
        String ipPort = parts[0];
        String payload = parts[1];
        parts = ipPort.split(":");
        WAF waf = new WAF();
        waf.setName(Tag.Unknown.name());
        // We gotta invent a name
        waf.setIpAddress(parts[0]);
        waf.setPortNumber(atoi(parts[1]));
        waf.setPayLoad(payload);
        waf.setUri(lines[1]);
        wafs.add(waf);
        return wafs;
    }

    public List<ServicePort> parseServicePorts(Context ctx, String portState, String protocol, Long portId, String output) {
        final List<ServicePort> servicePorts = new ArrayList<>();
        String[] lines = output.split("\n");
        for(int i = 0 ; i < lines.length; i++) {
            if(i == 0) {
                continue;
            }
            String line = tidy(lines[i]);
            String[] parts = line.split(" ");
            String name = extractName(parts[1]);
            String version = extractVersion(parts[1]);
            ServicePort sp = new ServicePort();
            sp.setPortNumber(portId);
            sp.setName(name);
            sp.setProduct(name);
            sp.setVersion(version);
            sp.setProtocol(protocol);
            sp.setScore(Integer.parseInt(parts[2]));
            sp.setHits(Integer.parseInt(parts[3]));
            sp.setPortState(portState);
            servicePorts.add(sp);
        }
        return servicePorts;
    }

    private String extractName(String str) {
        for(int i = 0 ; i < str.length(); i++) {
            if(Character.isDigit(str.charAt(i))) {
                return str.substring(0, i);
            }
        }
        return null;
    }
    private String extractVersion(String str) {
        for(int i = 0 ; i < str.length(); i++) {
            if(Character.isDigit(str.charAt(i))) {
                return str.substring(i);
            }
        }
        return null;
    }

    private String tidy(String line) {
        String text = line.replace("\t"," ");
        text = text.replaceAll(" +", " ");

        return text;
    }
}
