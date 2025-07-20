package org.publications.service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TypeDTO {

    private Long id;

    @NotEmpty
    private String name;
}
