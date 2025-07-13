package org.publications.service.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PublicationSpecificDTO {

    private Long id;

    private String title;

    private LocalDate publicationDate;

    private String isbnIssn;

    private String edition;

    private String abstractText;

    private Integer pageCount;

    private String category;

    private String type;

    private String publisher;

    private String language;
}
