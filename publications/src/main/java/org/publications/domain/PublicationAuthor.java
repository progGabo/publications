package org.publications.domain;

import jakarta.persistence.*;
import lombok.*;
import org.publications.domain.embeddedIds.PublicationAuthorId;

@Entity
@Table(name = "publication_author")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationAuthor {

    @EmbeddedId
    private PublicationAuthorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("publicationId")
    @JoinColumn(name = "publication_id")
    private Publication publication;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "author_order")
    private Integer authorOrder;
}