package org.publications.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;
}