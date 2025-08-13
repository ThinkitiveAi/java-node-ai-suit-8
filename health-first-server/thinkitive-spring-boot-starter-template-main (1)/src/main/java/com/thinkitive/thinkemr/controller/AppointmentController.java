package com.thinkitive.thinkemr.controller;

import com.thinkitive.thinkemr.dto.AppointmentBookingRequest;
import com.thinkitive.thinkemr.dto.AppointmentBookingResponse;
import com.thinkitive.thinkemr.dto.AppointmentListRequest;
import com.thinkitive.thinkemr.dto.AppointmentListResponse;
import com.thinkitive.thinkemr.entity.Appointment;
import com.thinkitive.thinkemr.repository.AppointmentRepository;
import com.thinkitive.thinkemr.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Booking", description = "APIs for appointment booking")
public class AppointmentController {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;

    @PostMapping("/book")
    @Operation(
        summary = "Book an appointment", 
        description = "Books an appointment between a patient and provider with validation for conflicts and business rules"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Appointment booked successfully",
            content = @Content(schema = @Schema(implementation = AppointmentBookingResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Bad Request - Invalid input or format"
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Conflict - Duplicate appointment (provider & time conflict)"
        ),
        @ApiResponse(
            responseCode = "422", 
            description = "Unprocessable Entity - Validation failure"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal Server Error - DB or system error"
        )
    })
    public ResponseEntity<AppointmentBookingResponse> bookAppointment(
            @Valid @RequestBody AppointmentBookingRequest request) {
        AppointmentBookingResponse response = appointmentService.bookAppointment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @Operation(
        summary = "List appointments", 
        description = "Retrieves a paginated list of appointments with filtering and sorting options"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Appointments retrieved successfully",
            content = @Content(schema = @Schema(implementation = AppointmentListResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Bad Request - Invalid parameters"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal Server Error"
        )
    })
    public ResponseEntity<AppointmentListResponse> listAppointments(
            @ModelAttribute AppointmentListRequest request) {
        try {
            AppointmentListResponse response = appointmentService.listAppointments(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error listing appointments: ", e);
            throw e;
        }
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Simple test endpoint")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Appointment controller is working!");
    }

    @GetMapping("/simple-list")
    @Operation(summary = "Simple appointment list", description = "Simple appointment listing without filters")
    public ResponseEntity<Map<String, Object>> simpleList() {
        try {
            List<Appointment> appointments = appointmentRepository.findAllActiveAppointments();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", appointments.size());
            response.put("appointments", appointments.stream()
                    .map(a -> Map.of(
                            "id", a.getId(),
                            "dateTime", a.getDateTime(),
                            "status", a.getStatus(),
                            "patientName", a.getPatient().getFirstName() + " " + a.getPatient().getLastName(),
                            "providerName", a.getProvider().getFirstName() + " " + a.getProvider().getLastName()
                    ))
                    .toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in simple list: ", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
} 