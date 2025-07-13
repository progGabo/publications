package org.publications.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publication_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}