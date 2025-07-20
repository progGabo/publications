package org.publications.service.dto.publications;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.publications.service.dto.AuthorDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class PublicationSpecificDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    @PastOrPresent
    private LocalDate publicationDate;

    @NotEmpty
    private String isbnIssn;

    @NotEmpty
    private String edition;

    @NotEmpty
    private String abstractText;

    @NotEmpty
    private Integer pageCount;

    @NotEmpty
    private Long categoryId;

    @NotEmpty
    private Long typeId;

    @NotEmpty
    private Long publisherId;

    @NotEmpty
    private Long languageId;

    @NotEmpty
    @Valid
    private List<AuthorDTO> authors;
}
