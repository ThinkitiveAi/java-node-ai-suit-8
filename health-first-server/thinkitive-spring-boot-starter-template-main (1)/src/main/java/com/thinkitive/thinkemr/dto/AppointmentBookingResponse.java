package com.thinkitive.thinkemr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentBookingResponse {
    @Schema(example = "true")
    private boolean success;

    @Schema(example = "Appointment booked successfully")
    private String message;

    @Schema(example = "2024-01-15T10:00:00")
    private LocalDateTime timestamp;

    private Data data;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Data {
        @Schema(example = "789e0123-e89b-12d3-a456-426614174002")
        private UUID appointmentId;

        @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID patientId;

        @Schema(example = "456e7890-e89b-12d3-a456-426614174001")
        private UUID providerId;

        @Schema(example = "scheduled")
        private String status;
    }
} 