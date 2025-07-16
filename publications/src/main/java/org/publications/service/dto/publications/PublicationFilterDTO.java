package org.publications.service.dto.publications;

import lombok.Data;

import java.util.List;

@Data
public class PublicationFilterDTO {

    private String searchTerm;

    private String publisher;

    private String category;

    private String type;

    private String language;

    private List<String> authorNames;
}