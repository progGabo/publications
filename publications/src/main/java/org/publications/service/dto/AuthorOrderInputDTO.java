package org.publications.service.dto;

import lombok.Data;

@Data
public class AuthorOrderInputDTO {
    private Long authorId;
    private Integer authorOrder;
}
