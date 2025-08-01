package com.thinkitive.thinkemr.service;

import com.thinkitive.thinkemr.dto.LoginRequest;
import com.thinkitive.thinkemr.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
} 