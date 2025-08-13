package com.thinkitive.thinkemr.dto;

import com.thinkitive.thinkemr.entity.AppointmentStatus;
import com.thinkitive.thinkemr.entity.AppointmentType;
import com.thinkitive.thinkemr.entity.AppointmentMode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AppointmentListRequest {
    @Schema(example = "1", description = "Page number (1-based)")
    private Integer page = 1;

    @Schema(example = "10", description = "Number of items per page")
    private Integer size = 10;

    @Schema(example = "dateTime", description = "Sort field: dateTime, patientName, providerName, status")
    private String sortBy = "dateTime";

    @Schema(example = "desc", description = "Sort direction: asc, desc")
    private String sortDirection = "desc";

    @Schema(example = "SCHEDULED", description = "Filter by appointment status")
    private AppointmentStatus status;

    @Schema(example = "NEW", description = "Filter by appointment type")
    private AppointmentType appointmentType;

    @Schema(example = "IN_PERSON", description = "Filter by appointment mode")
    private AppointmentMode mode;

    @Schema(example = "2025-08-01", description = "Filter by start date")
    private LocalDate startDate;

    @Schema(example = "2025-08-31", description = "Filter by end date")
    private LocalDate endDate;

    @Schema(example = "Maria", description = "Search by patient name")
    private String patientName;

    @Schema(example = "Dr. Jennifer", description = "Search by provider name")
    private String providerName;

    @Schema(example = "f4705e57-362f-4e98-93a8-c86b4deb96e7", description = "Filter by specific provider ID")
    private UUID providerId;

    @Schema(example = "96b027ba-bb30-4cff-a22b-4f878ec493e2", description = "Filter by specific patient ID")
    private UUID patientId;
} 