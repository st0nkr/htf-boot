package com.teto.domain.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "known_service")
public class KnownService {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "port_number")
    private Long portNumber;

    @Column(name = "comment")
    private String comment;

    @Column(name = "use_frequency")
    private double useFrequency;
}
