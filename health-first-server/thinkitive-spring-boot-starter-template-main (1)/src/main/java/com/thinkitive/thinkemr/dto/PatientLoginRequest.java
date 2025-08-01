package com.thinkitive.thinkemr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientLoginRequest {
    @Schema(example = "jane.smith@email.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Schema(example = "SecurePassword123!", required = true)
    @NotBlank
    private String password;
} 