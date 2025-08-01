package com.thinkitive.thinkemr.controller;

import com.thinkitive.thinkemr.dto.PatientRegistrationRequest;
import com.thinkitive.thinkemr.dto.PatientRegistrationResponse;
import com.thinkitive.thinkemr.dto.PatientLoginRequest;
import com.thinkitive.thinkemr.dto.PatientLoginResponse;
import com.thinkitive.thinkemr.service.PatientService;
import com.thinkitive.thinkemr.service.PatientAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
@Tag(name = "Patient Registration", description = "APIs for patient registration")
public class PatientController {
    private final PatientService patientService;
    private final PatientAuthService patientAuthService;

    @PostMapping("/register")
    @Operation(summary = "Register a new patient", description = "Registers a patient and returns patient ID, email, phone, and verification status.")
    public ResponseEntity<PatientRegistrationResponse> registerPatient(
            @Valid @RequestBody PatientRegistrationRequest request) {
        PatientRegistrationResponse response = patientService.registerPatient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Patient login", description = "Authenticate patient and return JWT access token.")
    public ResponseEntity<PatientLoginResponse> login(@Valid @RequestBody PatientLoginRequest request) {
        PatientLoginResponse response = patientAuthService.login(request);
        return ResponseEntity.ok(response);
    }
} 