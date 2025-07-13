package org.publications.service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PublicationDTO {
    private Long id;

    private String title;

    private String isbnIssn;

    private String edition;

    private String abstractText;

    private Integer pageCount;

    private String publisher;

    private LocalDate publicationDate;

    private String language;

    private String category;

    private String type;

    private List<AuthorInPublicationDTO> authors;
}