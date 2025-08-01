package com.thinkitive.thinkemr.service.impl;

import com.thinkitive.thinkemr.dto.PatientLoginRequest;
import com.thinkitive.thinkemr.dto.PatientLoginResponse;
import com.thinkitive.thinkemr.entity.Patient;
import com.thinkitive.thinkemr.repository.PatientRepository;
import com.thinkitive.thinkemr.service.PatientAuthService;
import com.thinkitive.thinkemr.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientAuthServiceImpl implements PatientAuthService {
    private static final Logger logger = LoggerFactory.getLogger(PatientAuthServiceImpl.class);
    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public PatientLoginResponse login(PatientLoginRequest request) {
        String email = StringUtils.trimWhitespace(request.getEmail());
        String password = request.getPassword();
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            logger.warn("Patient login failed: not found for email {}", email);
            throw new RuntimeException("INVALID_CREDENTIALS");
        }
        Patient patient = patientOpt.get();
        if (!patient.isActive()) {
            logger.warn("Patient login failed: not active for email {}", email);
            throw new RuntimeException("INVALID_CREDENTIALS");
        }
        if (!passwordEncoder.matches(password, patient.getPasswordHash())) {
            logger.warn("Patient login failed: invalid password for email {}", email);
            throw new RuntimeException("INVALID_CREDENTIALS");
        }
        String token = jwtUtil.generatePatientToken(patient);
        int expiresIn = jwtUtil.getPatientExpirationSeconds();
        PatientLoginResponse.PatientInfo patientInfo = new PatientLoginResponse.PatientInfo(
                patient.getId().toString(),
                patient.getEmail(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getGender().name().toLowerCase(),
                patient.isEmailVerified(),
                patient.isPhoneVerified(),
                patient.isActive()
        );
        PatientLoginResponse.Data data = new PatientLoginResponse.Data(token, expiresIn, "Bearer", patientInfo);
        return new PatientLoginResponse(true, "Login successful", data);
    }
} 