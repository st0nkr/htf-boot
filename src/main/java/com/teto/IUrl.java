package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.url.Url;

import java.util.*;

public interface IUrl extends IOptional{

    default Optional<List<Url>> getUrls(Context ctx, TargetNode node, Set<Provenance> provs) {
        Collection<Url> urls = node.getScannedTargets().getUrls();
        if(urls == null || urls.isEmpty()) {
            return empty();
        }
        final List<Url> urlList = new ArrayList<>(urls);
        final Set<String> names = new HashSet<>();
        provs.forEach(p -> {
            names.add(p.name());
        });
        for(Url url : urls) {
            if(names.contains(url.getProvenance())) {
                urlList.add(url);
            }
        }
        return optional(urlList);
    }

    default Optional<Collection<Url>> getMatchingUrls(Context ctx, TargetNode node, Set<Provenance> provs, String...endsWiths) {
        Optional<List<Url>> urls = getUrls(ctx, node, provs);
        if(urls.isEmpty()) {
            return empty();
        }
        final Collection<Url> matches = new TreeSet<>();
        for(Url url : urls.get()) {
            if(url.getUrl() != null) {
                String u = url.getUrl().toLowerCase();
                for(String endsWith : endsWiths) {
                    if (u.endsWith(endsWith.toLowerCase())) {
                        matches.add(url);
                    }
                }
            }
        }
        return optional(matches);
    }
}
