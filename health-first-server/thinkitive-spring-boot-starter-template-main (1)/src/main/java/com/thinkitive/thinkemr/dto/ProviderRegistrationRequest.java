package com.thinkitive.thinkemr.dto;

import com.thinkitive.thinkemr.entity.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderRegistrationRequest {
    @Schema(example = "John", required = true)
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @Schema(example = "Doe", required = true)
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Schema(example = "john.doe@example.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Schema(example = "+12345678901", required = true)
    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid international phone number format")
    private String phoneNumber;

    @Schema(example = "Password@123", required = true)
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain uppercase, lowercase, digit, and special character"
    )
    private String password;

    @Schema(example = "Password@123", required = true)
    @NotBlank
    private String confirmPassword;

    @Schema(example = "CARDIOLOGY", required = true)
    @NotNull
    private Specialization specialization;

    @Schema(example = "LIC12345", required = true)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "License number must be alphanumeric")
    private String licenseNumber;

    @Schema(example = "10", required = true)
    @Min(0)
    @Max(50)
    private int yearsOfExperience;

    @Valid
    @NotNull
    private ClinicAddressDto clinicAddress;

    @Getter
    @Setter
    public static class ClinicAddressDto {
        @Schema(example = "123 Main St", required = true)
        @NotBlank
        private String street;

        @Schema(example = "New York", required = true)
        @NotBlank
        private String city;

        @Schema(example = "NY", required = true)
        @NotBlank
        private String state;

        @Schema(example = "10001", required = true)
        @NotBlank
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid US ZIP code format")
        private String zip;
    }
} 