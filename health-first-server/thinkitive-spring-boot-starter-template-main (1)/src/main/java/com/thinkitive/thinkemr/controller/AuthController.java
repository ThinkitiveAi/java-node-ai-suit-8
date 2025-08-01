package com.thinkitive.thinkemr.controller;

import com.thinkitive.thinkemr.dto.LoginRequest;
import com.thinkitive.thinkemr.dto.LoginResponse;
import com.thinkitive.thinkemr.dto.ErrorResponse;
import com.thinkitive.thinkemr.service.AuthService;
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
@Tag(name = "Provider Authentication", description = "APIs for provider login and authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Provider login", description = "Authenticate provider and return JWT access token.")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
} 