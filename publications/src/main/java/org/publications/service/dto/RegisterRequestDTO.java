package org.publications.service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String password;
}
