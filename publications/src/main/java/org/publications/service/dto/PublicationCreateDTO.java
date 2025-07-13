package org.publications.service.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

@Getter
@Setter
@ToString
public class PublicationCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String isbnIssn;

    private LocalDate publicationDate;

    private String edition;

    private String abstractText;

    private Integer pageCount;

    private Long publisherId;

    private Long languageId;

    private Long categoryId;

    private Long typeId;

    private List<AuthorOrderInputDTO> authors;
}
