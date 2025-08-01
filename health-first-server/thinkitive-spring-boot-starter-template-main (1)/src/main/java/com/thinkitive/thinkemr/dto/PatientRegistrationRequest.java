package com.thinkitive.thinkemr.dto;

import com.thinkitive.thinkemr.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PatientRegistrationRequest {
    @Schema(example = "Jane", required = true)
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @Schema(example = "Smith", required = true)
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Schema(example = "jane.smith@email.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Schema(example = "+1234567890", required = true)
    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid international phone number format")
    private String phoneNumber;

    @Schema(example = "SecurePassword123!", required = true)
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain uppercase, lowercase, digit, and special character"
    )
    private String password;

    @Schema(example = "SecurePassword123!", required = true)
    @NotBlank
    private String confirmPassword;

    @Schema(example = "1990-05-15", required = true, type = "string", format = "date")
    @NotNull
    private LocalDate dateOfBirth;

    @Schema(example = "female", required = true)
    @NotNull
    private Gender gender;

    @Valid
    @NotNull
    private AddressDto address;

    @Valid
    private EmergencyContactDto emergencyContact;

    private List<@Size(max = 255) String> medicalHistory;

    @Valid
    private InsuranceInfoDto insuranceInfo;

    @Getter
    @Setter
    public static class AddressDto {
        @Schema(example = "456 Main Street", required = true)
        @NotBlank
        @Size(max = 200)
        private String street;

        @Schema(example = "Boston", required = true)
        @NotBlank
        @Size(max = 100)
        private String city;

        @Schema(example = "MA", required = true)
        @NotBlank
        @Size(max = 50)
        private String state;

        @Schema(example = "02101", required = true)
        @NotBlank
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid US ZIP code format")
        private String zip;
    }

    @Getter
    @Setter
    public static class EmergencyContactDto {
        @Schema(example = "John Smith")
        @NotBlank
        @Size(max = 100)
        private String name;

        @Schema(example = "+1234567891")
        @NotBlank
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid international phone number format")
        private String phone;

        @Schema(example = "spouse")
        @NotBlank
        @Size(max = 50)
        private String relationship;
    }

    @Getter
    @Setter
    public static class InsuranceInfoDto {
        @Schema(example = "Blue Cross")
        private String provider;

        @Schema(example = "BC123456789")
        private String policyNumber;
    }
} 