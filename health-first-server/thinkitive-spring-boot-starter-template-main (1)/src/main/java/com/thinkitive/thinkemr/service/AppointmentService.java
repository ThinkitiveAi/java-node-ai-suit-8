package com.thinkitive.thinkemr.service;

import com.thinkitive.thinkemr.dto.AppointmentBookingRequest;
import com.thinkitive.thinkemr.dto.AppointmentBookingResponse;
import com.thinkitive.thinkemr.dto.AppointmentListRequest;
import com.thinkitive.thinkemr.dto.AppointmentListResponse;

public interface AppointmentService {
    AppointmentBookingResponse bookAppointment(AppointmentBookingRequest request);
    AppointmentListResponse listAppointments(AppointmentListRequest request);
} 