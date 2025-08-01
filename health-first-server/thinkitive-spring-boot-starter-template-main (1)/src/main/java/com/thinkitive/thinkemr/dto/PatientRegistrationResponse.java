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
public class PatientRegistrationResponse {
    @Schema(example = "true")
    private boolean success;

    @Schema(example = "Patient registered successfully. Verification email sent.")
    private String message;

    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        @Schema(example = "uuid-here")
        private String patient_id;

        @Schema(example = "jane.smith@email.com")
        private String email;

        @Schema(example = "+1234567890")
        private String phone_number;

        @Schema(example = "false")
        private boolean email_verified;

        @Schema(example = "false")
        private boolean phone_verified;
    }
} 