package com.teto.domain.parser.linpeas;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinPeasFinding {
    private String category;
    private String title;
    private String details;
    private LinPeasSeverity severity;
    private String rawLine;
    private String mitreTechnique;

    @Override
    public String toString() {
        return "LinPeasFinding{" +
                "category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", severity=" + severity +
                ", mitreTechnique='" + mitreTechnique + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
