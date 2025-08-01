package com.thinkitive.thinkemr.service.impl;

import com.thinkitive.thinkemr.dto.ProviderRegistrationRequest;
import com.thinkitive.thinkemr.dto.ProviderRegistrationResponse;
import com.thinkitive.thinkemr.entity.*;
import com.thinkitive.thinkemr.repository.ProviderRepository;
import com.thinkitive.thinkemr.service.ProviderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {
    private static final Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);
    private final ProviderRepository providerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ProviderRegistrationResponse registerProvider(ProviderRegistrationRequest request) {
        // Sanitize and trim inputs
        String email = StringUtils.trimWhitespace(request.getEmail());
        String phone = StringUtils.trimWhitespace(request.getPhoneNumber());
        String license = StringUtils.trimWhitespace(request.getLicenseNumber());

        // Uniqueness checks
        if (providerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (providerRepository.existsByPhoneNumber(phone)) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        if (providerRepository.existsByLicenseNumber(license)) {
            throw new IllegalArgumentException("License number already exists");
        }
        // Password match check
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        // Hash password
        String passwordHash = passwordEncoder.encode(request.getPassword());

        // Map ClinicAddress
        ClinicAddress address = new ClinicAddress();
        address.setStreet(StringUtils.trimWhitespace(request.getClinicAddress().getStreet()));
        address.setCity(StringUtils.trimWhitespace(request.getClinicAddress().getCity()));
        address.setState(StringUtils.trimWhitespace(request.getClinicAddress().getState()));
        address.setZip(StringUtils.trimWhitespace(request.getClinicAddress().getZip()));

        // Build Provider entity
        Provider provider = Provider.builder()
                .firstName(StringUtils.trimWhitespace(request.getFirstName()))
                .lastName(StringUtils.trimWhitespace(request.getLastName()))
                .email(email)
                .phoneNumber(phone)
                .passwordHash(passwordHash)
                .specialization(request.getSpecialization())
                .licenseNumber(license)
                .yearsOfExperience(request.getYearsOfExperience())
                .clinicAddress(address)
                .verificationStatus(VerificationStatus.PENDING)
                .isActive(true)
                .build();
        provider = providerRepository.save(provider);
        logger.info("Provider registered: {}", provider.getId());
        return new ProviderRegistrationResponse(
                provider.getId().toString(),
                provider.getEmail(),
                provider.getVerificationStatus().name().toLowerCase()
        );
    }
} 