package com.thinkitive.thinkemr.service;

import com.thinkitive.thinkemr.dto.ProviderRegistrationRequest;
import com.thinkitive.thinkemr.dto.ProviderRegistrationResponse;

public interface ProviderService {
    ProviderRegistrationResponse registerProvider(ProviderRegistrationRequest request);
} 