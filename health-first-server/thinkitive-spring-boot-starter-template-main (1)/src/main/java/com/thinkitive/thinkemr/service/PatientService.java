package com.thinkitive.thinkemr.service;

import com.thinkitive.thinkemr.dto.PatientRegistrationRequest;
import com.thinkitive.thinkemr.dto.PatientRegistrationResponse;

public interface PatientService {
    PatientRegistrationResponse registerPatient(PatientRegistrationRequest request);
} 