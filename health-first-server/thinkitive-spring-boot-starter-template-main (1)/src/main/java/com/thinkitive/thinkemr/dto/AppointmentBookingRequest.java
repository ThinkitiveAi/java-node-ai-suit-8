package com.thinkitive.thinkemr.dto;

import com.thinkitive.thinkemr.entity.AppointmentMode;
import com.thinkitive.thinkemr.entity.AppointmentType;
import com.thinkitive.thinkemr.util.FutureAppointmentTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentBookingRequest {
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    @NotNull
    private UUID patientId;

    @Schema(example = "456e7890-e89b-12d3-a456-426614174001", required = true)
    @NotNull
    private UUID providerId;

    @Schema(example = "NEW", required = true)
    @NotNull
    private AppointmentType appointmentType;

    @Schema(example = "IN_PERSON", required = true)
    @NotNull
    private AppointmentMode mode;

    @Schema(example = "2024-01-15T10:00:00", required = true)
    @NotNull
    @FutureAppointmentTime
    private LocalDateTime dateTime;

    @Schema(example = "Annual checkup and blood work", required = true)
    @NotBlank
    @Size(min = 5, max = 250, message = "Reason for visit must be between 5 and 250 characters")
    private String reasonForVisit;

    @Schema(example = "150.00", required = true)
    @NotNull
    @DecimalMin(value = "0.01", message = "Estimated amount must be positive")
    @DecimalMax(value = "10000.00", message = "Estimated amount cannot exceed $10,000")
    private BigDecimal estimatedAmount;

    @Valid
    @NotNull
    private ClinicAddressDto clinicAddress;

    @Getter
    @Setter
    public static class ClinicAddressDto {
        @Schema(example = "123 Main Street", required = true)
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
} 