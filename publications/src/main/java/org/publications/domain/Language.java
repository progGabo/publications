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

    @Column(nullable = false, unique = true)
    private String name;
}