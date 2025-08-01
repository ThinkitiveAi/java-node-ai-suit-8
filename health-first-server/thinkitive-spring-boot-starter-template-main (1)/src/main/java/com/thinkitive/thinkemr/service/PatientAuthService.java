package com.thinkitive.thinkemr.service;

import com.thinkitive.thinkemr.dto.PatientLoginRequest;
import com.thinkitive.thinkemr.dto.PatientLoginResponse;

public interface PatientAuthService {
    PatientLoginResponse login(PatientLoginRequest request);
} 