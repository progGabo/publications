package org.publications.domain.embeddedIds;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationAuthorId implements Serializable {

    @Column(name = "publication_id")
    private Long publicationId;

    @Column(name = "author_id")
    private Long authorId;

}