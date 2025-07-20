package org.publications.service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String password;
}
