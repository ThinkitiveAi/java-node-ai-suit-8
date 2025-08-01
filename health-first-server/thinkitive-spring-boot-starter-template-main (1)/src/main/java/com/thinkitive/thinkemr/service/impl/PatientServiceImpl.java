package com.thinkitive.thinkemr.service.impl;

import com.thinkitive.thinkemr.dto.PatientRegistrationRequest;
import com.thinkitive.thinkemr.dto.PatientRegistrationResponse;
import com.thinkitive.thinkemr.entity.*;
import com.thinkitive.thinkemr.repository.PatientRepository;
import com.thinkitive.thinkemr.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);
    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PatientRegistrationResponse registerPatient(PatientRegistrationRequest request) {
        // Sanitize and trim inputs
        String email = StringUtils.trimWhitespace(request.getEmail());
        String phone = StringUtils.trimWhitespace(request.getPhoneNumber());

        // Uniqueness checks
        if (patientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }
        if (patientRepository.existsByPhoneNumber(phone)) {
            throw new IllegalArgumentException("Phone number is already registered");
        }
        // Password match check
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        // Age check (must be at least 13)
        LocalDate dob = request.getDateOfBirth();
        if (dob == null || Period.between(dob, LocalDate.now()).getYears() < 13) {
            throw new IllegalArgumentException("Must be at least 13 years old");
        }
        // Hash password
        String passwordHash = passwordEncoder.encode(request.getPassword());

        // Map Address
        Address address = new Address();
        address.setStreet(StringUtils.trimWhitespace(request.getAddress().getStreet()));
        address.setCity(StringUtils.trimWhitespace(request.getAddress().getCity()));
        address.setState(StringUtils.trimWhitespace(request.getAddress().getState()));
        address.setZip(StringUtils.trimWhitespace(request.getAddress().getZip()));

        // Map EmergencyContact
        EmergencyContact emergencyContact = null;
        if (request.getEmergencyContact() != null) {
            emergencyContact = new EmergencyContact();
            emergencyContact.setName(StringUtils.trimWhitespace(request.getEmergencyContact().getName()));
            emergencyContact.setPhone(StringUtils.trimWhitespace(request.getEmergencyContact().getPhone()));
            emergencyContact.setRelationship(StringUtils.trimWhitespace(request.getEmergencyContact().getRelationship()));
        }

        // Map InsuranceInfo
        InsuranceInfo insuranceInfo = null;
        if (request.getInsuranceInfo() != null) {
            insuranceInfo = new InsuranceInfo();
            insuranceInfo.setProvider(StringUtils.trimWhitespace(request.getInsuranceInfo().getProvider()));
            insuranceInfo.setPolicyNumber(StringUtils.trimWhitespace(request.getInsuranceInfo().getPolicyNumber()));
        }

        // Build Patient entity
        Patient patient = Patient.builder()
                .firstName(StringUtils.trimWhitespace(request.getFirstName()))
                .lastName(StringUtils.trimWhitespace(request.getLastName()))
                .email(email)
                .phoneNumber(phone)
                .passwordHash(passwordHash)
                .dateOfBirth(dob)
                .gender(request.getGender())
                .address(address)
                .emergencyContact(emergencyContact)
                .medicalHistory(request.getMedicalHistory())
                .insuranceInfo(insuranceInfo)
                .emailVerified(false)
                .phoneVerified(false)
                .isActive(true)
                .build();
        patient = patientRepository.save(patient);
        logger.info("Patient registered: {}", patient.getId());
        PatientRegistrationResponse.Data data = new PatientRegistrationResponse.Data(
                patient.getId().toString(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.isEmailVerified(),
                patient.isPhoneVerified()
        );
        return new PatientRegistrationResponse(true, "Patient registered successfully. Verification email sent.", data);
    }
} 