package org.publications.domain;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "publication")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Column(name = "isbn_issn", unique = true, nullable = false)
    private String isbnIssn;

    @Column(name = "edition", nullable = false)
    private String edition;

    @Column(name = "abstract", nullable = false)
    private String abstractText;

    @Column(name = "page_count",nullable = false)
    private Integer pageCount;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PublicationType type;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToMany
    @JoinTable(
            name = "publication_author",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();

}