package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.linpeas.LinPeasResult;
import com.teto.domain.parser.linpeas.LinPeasSection;
import com.teto.domain.parser.linpeas.LinPeasSubsection;

import java.util.*;

public interface IPeas {
    default LinPeasResult getPeas(Context ctx, TargetNode node) {
        return node.getPeas();
    }

    default List<LinPeasSection> filterSections(Context ctx, TargetNode node, Set<String> names) {
        final List<LinPeasSection> sections = new ArrayList<>();
        for(LinPeasSection section : getPeas(ctx, node).getSections()) {
            if(names.contains("all")) {
                sections.add(section);
            } else {
                for (String name : names) {
                    if (section.getName().toLowerCase().contains(name.toLowerCase())) {
                        sections.add(section);
                    }
                }
            }
        }
        return sections;
    }

    default List<LinPeasSubsection> filterSubSections(List<LinPeasSubsection> subsections, Set<String> subs) {
        if(subs.contains("all")) {
            return subsections;
        }
        final List<LinPeasSubsection> subbers = new ArrayList<>();
        for(LinPeasSubsection ss : subsections) {
            for(String sub : subs) {
                if(ss.getTitle().toLowerCase().contains(sub.toLowerCase())) {
                    subbers.add(ss);
                }
            }
        }
        return subbers;
    }

    default List<String> filterLines(List<String> lines, Set<String> keyWords) {
        if(keyWords.contains("all")) {
            return lines;
        }
        final List<String> result = new ArrayList<>();
        for(String line : lines) {
            for(String keyWord : keyWords) {
                if(line.toLowerCase().contains(keyWord.toLowerCase())) {
                    result.add(line);
                }
            }
        }
        return result;
    }
    default Collection<String> lines(Context ctx, TargetNode node, Set<String> sects, Set<String> subs, Set<String> keyWords) {
        final List<LinPeasSection> sections = filterSections(ctx, node, sects);
        final Collection<String> lines = new TreeSet<>();
        for(LinPeasSection section : sections) {
            List<LinPeasSubsection> subSections = filterSubSections(section.getSubsections(), subs);
            if(subSections == null || subSections.isEmpty()) {
                continue;
            }

            for(LinPeasSubsection ss : subSections) {
                if(ss.getLines() == null || ss.getLines().isEmpty()) {
                    continue;
                }
                lines.addAll(filterLines(ss.getLines(), keyWords));
            }
        }
        return lines;
    }

}
