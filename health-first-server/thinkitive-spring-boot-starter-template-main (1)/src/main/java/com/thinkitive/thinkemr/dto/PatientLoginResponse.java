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
public class PatientLoginResponse {
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

        @Schema(example = "1800")
        private int expires_in;

        @Schema(example = "Bearer")
        private String token_type;

        private PatientInfo patient;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientInfo {
        @Schema(example = "uuid")
        private String id;

        @Schema(example = "jane.smith@email.com")
        private String email;

        @Schema(example = "Jane")
        private String first_name;

        @Schema(example = "Smith")
        private String last_name;

        @Schema(example = "female")
        private String gender;

        @Schema(example = "false")
        private boolean email_verified;

        @Schema(example = "false")
        private boolean phone_verified;

        @Schema(example = "true")
        private boolean is_active;
    }
} 