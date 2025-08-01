package com.thinkitive.thinkemr.service.impl;

import com.thinkitive.thinkemr.dto.LoginRequest;
import com.thinkitive.thinkemr.dto.LoginResponse;
import com.thinkitive.thinkemr.entity.Provider;
import com.thinkitive.thinkemr.entity.VerificationStatus;
import com.thinkitive.thinkemr.repository.ProviderRepository;
import com.thinkitive.thinkemr.service.AuthService;
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
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final ProviderRepository providerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        String email = StringUtils.trimWhitespace(request.getEmail());
        String password = request.getPassword();
        Optional<Provider> providerOpt = providerRepository.findByEmail(email);
        if (providerOpt.isEmpty()) {
            logger.warn("Login failed: provider not found for email {}", email);
            throw new RuntimeException("INVALID_CREDENTIALS");
        }
        Provider provider = providerOpt.get();
        if (!provider.isActive() || provider.getVerificationStatus() != VerificationStatus.VERIFIED) {
            logger.warn("Login failed: provider not active or not verified for email {}", email);
            throw new RuntimeException("INVALID_CREDENTIALS");
        }
        if (!passwordEncoder.matches(password, provider.getPasswordHash())) {
            logger.warn("Login failed: invalid password for email {}", email);
            throw new RuntimeException("INVALID_CREDENTIALS");
        }
        String token = jwtUtil.generateToken(provider);
        int expiresIn = jwtUtil.getExpirationSeconds();
        LoginResponse.ProviderInfo providerInfo = new LoginResponse.ProviderInfo(
                provider.getId().toString(),
                provider.getEmail(),
                provider.getFirstName(),
                provider.getLastName(),
                provider.getSpecialization().name().substring(0, 1) + provider.getSpecialization().name().substring(1).toLowerCase(),
                provider.getVerificationStatus().name().toLowerCase()
        );
        LoginResponse.Data data = new LoginResponse.Data(token, expiresIn, "Bearer", providerInfo);
        return new LoginResponse(true, "Login successful", data);
    }
} 