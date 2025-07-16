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

    @NotNull(message = "title is mandatory")
    private String title;

    @NotNull
    @PastOrPresent
    private LocalDate publicationDate;

    private String isbnIssn;

    private String edition;

    @NotNull
    private String abstractText;

    @NotNull
    private Integer pageCount;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long typeId;

    @NotNull
    private Long publisherId;

    @NotNull
    private Long languageId;

    @NotEmpty
    @Valid
    private List<AuthorDTO> authors;
}
