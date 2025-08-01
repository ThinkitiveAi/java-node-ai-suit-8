package com.thinkitive.thinkemr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @Schema(example = "true")
    private boolean success;

    @Schema(example = "Login successful")
    private String message;

    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        @Schema(example = "jwt-access-token-here")
        private String access_token;

        @Schema(example = "3600")
        private int expires_in;

        @Schema(example = "Bearer")
        private String token_type;

        private ProviderInfo provider;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderInfo {
        @Schema(example = "provider-uuid")
        private String id;

        @Schema(example = "john.doe@clinic.com")
        private String email;

        @Schema(example = "John")
        private String first_name;

        @Schema(example = "Doe")
        private String last_name;

        @Schema(example = "Cardiology")
        private String specialization;

        @Schema(example = "verified")
        private String verification_status;
    }
} 