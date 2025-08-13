package com.thinkitive.thinkemr.dto;

import com.thinkitive.thinkemr.entity.AppointmentStatus;
import com.thinkitive.thinkemr.entity.AppointmentType;
import com.thinkitive.thinkemr.entity.AppointmentMode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentListResponse {
    @Schema(example = "true")
    private boolean success;

    @Schema(example = "Appointments retrieved successfully")
    private String message;

    @Schema(example = "2025-08-08T16:00:25")
    private LocalDateTime timestamp;

    private Data data;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Data {
        private List<AppointmentItem> appointments;
        private PaginationInfo pagination;
        private SummaryInfo summary;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AppointmentItem {
        @Schema(example = "b42dcb10-8f82-4f5c-8803-0bb59f25acc2")
        private UUID appointmentId;

        @Schema(example = "2025-08-09T10:00:00")
        private LocalDateTime dateTime;

        @Schema(example = "NEW")
        private AppointmentType appointmentType;

        @Schema(example = "IN_PERSON")
        private AppointmentMode mode;

        @Schema(example = "Maria Garcia")
        private String patientName;

        @Schema(example = "FEMALE")
        private String patientGender;

        @Schema(example = "1987-03-10")
        private LocalDate patientDateOfBirth;

        @Schema(example = "38")
        private Integer patientAge;

        @Schema(example = "+1666777889")
        private String patientPhone;

        @Schema(example = "Dr. Jennifer Martinez")
        private String providerName;

        @Schema(example = "PSYCHIATRY")
        private String providerSpecialization;

        @Schema(example = "Initial consultation and health assessment")
        private String reasonForVisit;

        @Schema(example = "150.00")
        private BigDecimal estimatedAmount;

        @Schema(example = "SCHEDULED")
        private AppointmentStatus status;

        @Schema(example = "true")
        private boolean canStart;

        @Schema(example = "true")
        private boolean canEdit;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PaginationInfo {
        @Schema(example = "1")
        private int currentPage;

        @Schema(example = "10")
        private int pageSize;

        @Schema(example = "100")
        private long totalItems;

        @Schema(example = "10")
        private int totalPages;

        @Schema(example = "true")
        private boolean hasNext;

        @Schema(example = "false")
        private boolean hasPrevious;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SummaryInfo {
        @Schema(example = "11")
        private long totalAppointments;

        @Schema(example = "5")
        private long scheduledCount;

        @Schema(example = "3")
        private long checkedInCount;

        @Schema(example = "2")
        private long inExamCount;

        @Schema(example = "1")
        private long cancelledCount;
    }
} 