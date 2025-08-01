package com.thinkitive.thinkemr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProviderRegistrationResponse {
    @Schema(example = "b3b6c7e2-8c2a-4e2a-9b2a-1b2a3c4d5e6f")
    private String providerId;

    @Schema(example = "john.doe@example.com")
    private String email;

    @Schema(example = "pending")
    private String status;
} 