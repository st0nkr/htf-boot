package com.teto.domain.parser.nmap;

import com.teto.ICSV;
import com.teto.Teto;
import com.teto.command.Context;
import com.teto.command.regex.ExtractTargetTypes;
import com.teto.domain.nmap.HostVersionOS;
import com.teto.domain.nmap.NSEInfo;
import com.teto.domain.target.TargetType;

import java.util.*;

public class NMapOutputParser implements ICSV {
    private final String text;
    private final String prov;
    public NMapOutputParser(String text, String prov) {
        this.text = text;
        this.prov = prov;
    }

    public NSEInfo parse() {
        Context ctx = Teto.getContext();

        String[] lines = text.split("\n");
        final Map<TargetType, Collection<String>> targets = new HashMap<>();
        boolean isCertificate = false;
        for(String line : lines) {
            if (line.contains("-----BEGIN CERTIFICATE-----")) {
                isCertificate = true;
                break;
            }
        }

        NSEInfo info = new NSEInfo();
        if(!isCertificate) {
            Optional<Map<TargetType, Collection<String>>> targs = ctx.apply(new ExtractTargetTypes(text));
            if (targs.isPresent() && !targs.get().isEmpty()) {
                for (TargetType key : targs.get().keySet()) {
                    Collection<String> col = targets.computeIfAbsent(key, k -> new HashSet<>());
                    for (String item : targs.get().get(key)) {
                        if (item.startsWith("DNS:")) {
                            String[] parts = item.split(":");
                            col.add(parts[1]);
                        }
                    }
                }
            }


            if (!targets.isEmpty()) {
                info.setTargets(targets);
                Map<TargetType, Collection<String>> map = targets;
                if (mapContains(map, TargetType.Host, 1) && mapContains(map, TargetType.VersionNumber, 1) && mapContains(map, TargetType.Bracketed, 1)) {
                    HostVersionOS hvo = extractHVO(map);
                    if (hvo != null) {
                        info.getHostVersionOs().add(hvo);
                    }
                }
            }
        }

        return info;
    }

    private HostVersionOS extractHVO(Map<TargetType, Collection<String>> map) {
        HostVersionOS hvo = new HostVersionOS();
        hvo.setHost(mapExtract(map, TargetType.Host, 1));
        hvo.setVersion(mapExtract(map, TargetType.VersionNumber, 1));
        String os = removeFirstAndLast(mapExtract(map,TargetType.Bracketed,1));
        hvo.setOs(os);
        return hvo;
    }

    private String mapExtract(Map<TargetType, Collection<String>> map, TargetType tt, int index) {
        List<String> col = new ArrayList<>(map.get(tt));
        return col.get(index-1);
    }
    private boolean mapContains(Map<TargetType, Collection<String>> map, TargetType tt, int count) {
        Collection<String> col = map.get(tt);
        if(col == null) {
            return false;
        }
        return col.size() == count;
    }
}
