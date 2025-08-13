package com.thinkitive.thinkemr.service.impl;

import com.thinkitive.thinkemr.dto.AppointmentBookingRequest;
import com.thinkitive.thinkemr.dto.AppointmentBookingResponse;
import com.thinkitive.thinkemr.dto.AppointmentListRequest;
import com.thinkitive.thinkemr.dto.AppointmentListResponse;
import com.thinkitive.thinkemr.entity.*;
import com.thinkitive.thinkemr.repository.AppointmentRepository;
import com.thinkitive.thinkemr.repository.PatientRepository;
import com.thinkitive.thinkemr.repository.ProviderRepository;
import com.thinkitive.thinkemr.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ProviderRepository providerRepository;

    @Override
    @Transactional
    public AppointmentBookingResponse bookAppointment(AppointmentBookingRequest request) {
        logger.info("Booking appointment for patient: {} and provider: {}", request.getPatientId(), request.getProviderId());
        
        // Validate patient exists and is active
        Patient patient = validateAndGetPatient(request.getPatientId());
        
        // Validate provider exists and is active
        Provider provider = validateAndGetProvider(request.getProviderId());
        
        // Validate appointment time is in the future
        validateAppointmentTime(request.getDateTime());
        
        // Check for appointment conflicts (30-minute buffer)
        validateNoConflicts(provider.getId(), request.getDateTime());
        
        // Create clinic address
        ClinicAddress clinicAddress = createClinicAddress(request.getClinicAddress());
        
        // Build and save appointment
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .provider(provider)
                .appointmentType(request.getAppointmentType())
                .mode(request.getMode())
                .dateTime(request.getDateTime())
                .reasonForVisit(StringUtils.trimWhitespace(request.getReasonForVisit()))
                .estimatedAmount(request.getEstimatedAmount())
                .clinicAddress(clinicAddress)
                .status(AppointmentStatus.SCHEDULED)
                .isActive(true)
                .build();
        
        appointment = appointmentRepository.save(appointment);
        
        logger.info("Appointment booked: {} for patient: {} with provider: {} at {}", 
                appointment.getId(), patient.getId(), provider.getId(), request.getDateTime());
        
        // Create response
        AppointmentBookingResponse.Data data = new AppointmentBookingResponse.Data(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getProvider().getId(),
                appointment.getStatus().name().toLowerCase()
        );
        
        return new AppointmentBookingResponse(
                true,
                "Appointment booked successfully",
                LocalDateTime.now(),
                data
        );
    }

    @Override
    public AppointmentListResponse listAppointments(AppointmentListRequest request) {
        logger.info("Listing appointments");
        
        try {
            // Get all active appointments
            List<Appointment> allAppointments = appointmentRepository.findAllActiveAppointments();
            logger.info("Found {} appointments", allAppointments.size());
            
            // Convert to DTOs
            List<AppointmentListResponse.AppointmentItem> appointmentItems = allAppointments.stream()
                    .map(this::convertToAppointmentItem)
                    .toList();
            
            // Create simple pagination info
            AppointmentListResponse.PaginationInfo paginationInfo = new AppointmentListResponse.PaginationInfo(
                    1, allAppointments.size(), allAppointments.size(), 1, false, false
            );
            
            // Create simple summary info
            AppointmentListResponse.SummaryInfo summaryInfo = new AppointmentListResponse.SummaryInfo(
                    allAppointments.size(), 0, 0, 0, 0
            );
            
            // Create response data
            AppointmentListResponse.Data data = new AppointmentListResponse.Data(
                    appointmentItems, paginationInfo, summaryInfo
            );
            
            return new AppointmentListResponse(
                    true,
                    "Appointments retrieved successfully",
                    LocalDateTime.now(),
                    data
            );
        } catch (Exception e) {
            logger.error("Error in listAppointments: ", e);
            throw e;
        }
    }
    
    private AppointmentListResponse.AppointmentItem convertToAppointmentItem(Appointment appointment) {
        // Calculate patient age
        int patientAge = calculateAge(appointment.getPatient().getDateOfBirth());
        
        // Determine action permissions
        boolean canStart = appointment.getStatus() == AppointmentStatus.SCHEDULED || 
                          appointment.getStatus() == AppointmentStatus.CHECKED_IN;
        boolean canEdit = appointment.getStatus() != AppointmentStatus.CANCELLED;
        
        return new AppointmentListResponse.AppointmentItem(
                appointment.getId(),
                appointment.getDateTime(),
                appointment.getAppointmentType(),
                appointment.getMode(),
                appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                appointment.getPatient().getGender().name(),
                appointment.getPatient().getDateOfBirth(),
                patientAge,
                appointment.getPatient().getPhoneNumber(),
                "Dr. " + appointment.getProvider().getFirstName() + " " + appointment.getProvider().getLastName(),
                appointment.getProvider().getSpecialization().name(),
                appointment.getReasonForVisit(),
                appointment.getEstimatedAmount(),
                appointment.getStatus(),
                canStart,
                canEdit
        );
    }
    
    private int calculateAge(LocalDate dateOfBirth) {
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    private AppointmentListResponse.SummaryInfo getSummaryInfo() {
        List<Object[]> statusCounts = appointmentRepository.getAppointmentStatusCounts();
        
        long totalAppointments = 0;
        long scheduledCount = 0;
        long checkedInCount = 0;
        long inExamCount = 0;
        long cancelledCount = 0;
        
        for (Object[] statusCount : statusCounts) {
            AppointmentStatus status = (AppointmentStatus) statusCount[0];
            Long count = (Long) statusCount[1];
            totalAppointments += count;
            
            switch (status) {
                case SCHEDULED -> scheduledCount = count;
                case CHECKED_IN -> checkedInCount = count;
                case IN_EXAM -> inExamCount = count;
                case CANCELLED -> cancelledCount = count;
            }
        }
        
        return new AppointmentListResponse.SummaryInfo(
                totalAppointments, scheduledCount, checkedInCount, inExamCount, cancelledCount
        );
    }
    
    private Patient validateAndGetPatient(UUID patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        logger.info("Validating patient with ID: {}", patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }
        
        Patient patient = patientOpt.get();
        if (!patient.isActive()) {
            throw new IllegalArgumentException("Patient is not active: " + patientId);
        }
        
        return patient;
    }
    
    private Provider validateAndGetProvider(UUID providerId) {
        if (providerId == null) {
            throw new IllegalArgumentException("Provider ID cannot be null");
        }
        logger.info("Validating provider with ID: {}", providerId);
        Optional<Provider> providerOpt = providerRepository.findById(providerId);
        if (providerOpt.isEmpty()) {
            throw new IllegalArgumentException("Provider not found with ID: " + providerId);
        }
        
        Provider provider = providerOpt.get();
        if (!provider.isActive()) {
            throw new IllegalArgumentException("Provider is not active: " + providerId);
        }
        
        return provider;
    }
    
    private void validateAppointmentTime(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment time must be in the future");
        }
    }
    
    private void validateNoConflicts(UUID providerId, LocalDateTime appointmentTime) {
        // Define appointment duration (30 minutes)
        LocalDateTime startTime = appointmentTime.minusMinutes(30);
        LocalDateTime endTime = appointmentTime.plusMinutes(30);
        
        if (appointmentRepository.existsConflictingAppointment(providerId, startTime, endTime)) {
            throw new IllegalArgumentException("Appointment time conflicts with existing appointment for this provider");
        }
    }
    
    private ClinicAddress createClinicAddress(AppointmentBookingRequest.ClinicAddressDto addressDto) {
        ClinicAddress clinicAddress = new ClinicAddress();
        clinicAddress.setStreet(StringUtils.trimWhitespace(addressDto.getStreet()));
        clinicAddress.setCity(StringUtils.trimWhitespace(addressDto.getCity()));
        clinicAddress.setState(StringUtils.trimWhitespace(addressDto.getState()));
        clinicAddress.setZip(StringUtils.trimWhitespace(addressDto.getZip()));
        return clinicAddress;
    }
} 