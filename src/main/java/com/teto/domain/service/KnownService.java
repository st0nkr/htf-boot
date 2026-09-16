package com.teto.domain.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class KnownService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String protocol;

    private Long portNumber;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private double useFrequency;

    @Override
    public String toString() {
        return "KnownService{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", protocol='" + protocol + '\'' +
                ", portNumber=" + portNumber +
                ", comment='" + comment + '\'' +
                ", useFrequency=" + useFrequency +
                '}';
    }
}
