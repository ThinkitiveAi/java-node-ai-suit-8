package com.thinkitive.thinkemr.controller;

import com.thinkitive.thinkemr.dto.ProviderRegistrationRequest;
import com.thinkitive.thinkemr.dto.ProviderRegistrationResponse;
import com.thinkitive.thinkemr.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/provider")
@RequiredArgsConstructor
@Tag(name = "Provider Registration", description = "APIs for provider registration")
public class ProviderController {
    private final ProviderService providerService;

    @PostMapping("/register")
    @Operation(summary = "Register a new provider", description = "Registers a provider and returns provider ID, email, and status.")
    public ResponseEntity<ProviderRegistrationResponse> registerProvider(
            @Valid @RequestBody ProviderRegistrationRequest request) {
        ProviderRegistrationResponse response = providerService.registerProvider(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
} 