package com.thinkitive.thinkemr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkitive.thinkemr.dto.AppointmentBookingRequest;
import com.thinkitive.thinkemr.entity.*;
import com.thinkitive.thinkemr.repository.AppointmentRepository;
import com.thinkitive.thinkemr.repository.PatientRepository;
import com.thinkitive.thinkemr.repository.ProviderRepository;
import com.thinkitive.thinkemr.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class AppointmentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Patient testPatient;
    private Provider testProvider;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        // Create test patient
        testPatient = createTestPatient();
        testPatient = patientRepository.save(testPatient);
        
        // Create test provider
        testProvider = createTestProvider();
        testProvider = providerRepository.save(testProvider);
    }

    @Test
    void testBookAppointment_Success() throws Exception {
        AppointmentBookingRequest request = createValidAppointmentRequest();
        
        mockMvc.perform(post("/api/v1/appointments/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Appointment booked successfully"))
                .andExpect(jsonPath("$.data.appointmentId").exists())
                .andExpect(jsonPath("$.data.patientId").value(testPatient.getId().toString()))
                .andExpect(jsonPath("$.data.providerId").value(testProvider.getId().toString()))
                .andExpect(jsonPath("$.data.status").value("scheduled"));
    }

    @Test
    void testBookAppointment_InvalidPatientId() throws Exception {
        AppointmentBookingRequest request = createValidAppointmentRequest();
        request.setPatientId(UUID.randomUUID()); // Non-existent patient ID
        
        mockMvc.perform(post("/api/v1/appointments/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Patient not found with ID: " + request.getPatientId()));
    }

    @Test
    void testBookAppointment_InvalidProviderId() throws Exception {
        AppointmentBookingRequest request = createValidAppointmentRequest();
        request.setProviderId(UUID.randomUUID()); // Non-existent provider ID
        
        mockMvc.perform(post("/api/v1/appointments/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Provider not found with ID: " + request.getProviderId()));
    }

    @Test
    void testBookAppointment_PastDateTime() throws Exception {
        AppointmentBookingRequest request = createValidAppointmentRequest();
        request.setDateTime(LocalDateTime.now().minusHours(1)); // Past time
        
        mockMvc.perform(post("/api/v1/appointments/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Appointment time must be in the future"));
    }

    @Test
    void testBookAppointment_InvalidZipCode() throws Exception {
        AppointmentBookingRequest request = createValidAppointmentRequest();
        request.getClinicAddress().setZip("invalid"); // Invalid ZIP code
        
        mockMvc.perform(post("/api/v1/appointments/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBookAppointment_InvalidAmount() throws Exception {
        AppointmentBookingRequest request = createValidAppointmentRequest();
        request.setEstimatedAmount(new BigDecimal("15000.00")); // Exceeds $10,000
        
        mockMvc.perform(post("/api/v1/appointments/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private AppointmentBookingRequest createValidAppointmentRequest() {
        AppointmentBookingRequest request = new AppointmentBookingRequest();
        request.setPatientId(testPatient.getId());
        request.setProviderId(testProvider.getId());
        request.setAppointmentType(AppointmentType.NEW);
        request.setMode(AppointmentMode.IN_PERSON);
        request.setDateTime(LocalDateTime.now().plusDays(1).plusHours(10)); // Tomorrow at 10 AM
        request.setReasonForVisit("Annual checkup and blood work");
        request.setEstimatedAmount(new BigDecimal("150.00"));
        
        AppointmentBookingRequest.ClinicAddressDto address = new AppointmentBookingRequest.ClinicAddressDto();
        address.setStreet("123 Main Street");
        address.setCity("Boston");
        address.setState("MA");
        address.setZip("02101");
        request.setClinicAddress(address);
        
        return request;
    }

    private Patient createTestPatient() {
        Address address = new Address();
        address.setStreet("456 Test Street");
        address.setCity("Test City");
        address.setState("TS");
        address.setZip("12345");
        
        return Patient.builder()
                .firstName("Test")
                .lastName("Patient")
                .email("test.patient@example.com")
                .phoneNumber("+1234567890")
                .passwordHash("hashedPassword")
                .dateOfBirth(java.time.LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .address(address)
                .isActive(true)
                .build();
    }

    private Provider createTestProvider() {
        ClinicAddress clinicAddress = new ClinicAddress();
        clinicAddress.setStreet("789 Provider Street");
        clinicAddress.setCity("Provider City");
        clinicAddress.setState("PC");
        clinicAddress.setZip("54321");
        
        return Provider.builder()
                .firstName("Test")
                .lastName("Provider")
                .email("test.provider@example.com")
                .phoneNumber("+0987654321")
                .passwordHash("hashedPassword")
                .specialization(Specialization.CARDIOLOGY)
                .licenseNumber("LIC123456")
                .yearsOfExperience(10)
                .clinicAddress(clinicAddress)
                .verificationStatus(VerificationStatus.VERIFIED)
                .isActive(true)
                .build();
    }
} 