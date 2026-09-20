package com.teto.domain.parser.linpeas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LinPeasSubsection {
    private String title;
    private String tag;
    private List<String> lines = new ArrayList<>();
    private List<LinPeasFinding> findings = new ArrayList<>();

    public LinPeasSubsection(String title, String tag) {
        this.title = title;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "LinPeasSubsection{" +
                "title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", linesCount=" + lines.size() +
                ", findingsCount=" + findings.size() +
                '}';
    }
}
