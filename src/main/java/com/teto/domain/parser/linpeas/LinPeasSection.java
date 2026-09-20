package com.teto.domain.parser.linpeas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LinPeasSection {
    private String name;
    private List<String> lines = new ArrayList<>();
    private List<LinPeasSubsection> subsections = new ArrayList<>();

    public LinPeasSection(String name) {
        this.name = name;
    }

    public LinPeasSubsection findSubsection(String titleKeyword) {
        if (titleKeyword == null || subsections == null) {
            return null;
        }
        for (LinPeasSubsection sub : subsections) {
            if (sub.getTitle() != null && sub.getTitle().toLowerCase().contains(titleKeyword.toLowerCase())) {
                return sub;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "LinPeasSection{" +
                "name='" + name + '\'' +
                ", subsectionsCount=" + subsections.size() +
                ", linesCount=" + lines.size() +
                '}';
    }
}
