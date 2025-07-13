package org.publications.service.dto;

import lombok.Data;

@Data
public class AuthorInPublicationDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer authorOrder;
}