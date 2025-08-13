# Appointment Booking API

A production-grade Appointment Booking API built with Spring Boot 3.1.x for the ThinkEMR system.

## Tech Stack

- **Java 17 LTS**
- **Spring Boot 3.2.5**
- **Spring Data JPA** (PostgreSQL 15.x)
- **Spring Security 6.x** with JWT
- **Hibernate Validator**
- **Maven 3.9.x**
- **SLF4J** with Logback
- **SpringDoc OpenAPI 3** (Swagger UI)

## API Endpoint

### POST /api/v1/appointments/book

Books an appointment between a patient and a provider.

#### Request Body

```json
{
  "patient_id": "123e4567-e89b-12d3-a456-426614174000",
  "provider_id": "456e7890-e89b-12d3-a456-426614174001",
  "appointment_type": "NEW",
  "mode": "IN_PERSON",
  "date_time": "2024-01-15T10:00:00",
  "reason_for_visit": "Annual checkup and blood work",
  "estimated_amount": 150.00,
  "clinic_address": {
    "street": "123 Main Street",
    "city": "Boston",
    "state": "MA",
    "zip": "02101"
  }
}
```

#### Response (201 Created)

```json
{
  "success": true,
  "message": "Appointment booked successfully",
  "timestamp": "2024-01-15T09:30:00",
  "data": {
    "appointment_id": "789e0123-e89b-12d3-a456-426614174002",
    "patient_id": "123e4567-e89b-12d3-a456-426614174000",
    "provider_id": "456e7890-e89b-12d3-a456-426614174001",
    "status": "scheduled"
  }
}
```

## Validation Rules

| Field | Rule |
|-------|------|
| `patient_id` | Must be a valid UUID of an active patient |
| `provider_id` | Must be a valid UUID of an active provider |
| `appointment_type` | Must be `NEW` or `FOLLOW_UP` |
| `mode` | Must be `IN_PERSON`, `VIDEO_CALL`, or `HOME` |
| `date_time` | Must be at least 1 hour in the future |
| `reason_for_visit` | Required, 5-250 characters |
| `estimated_amount` | Positive decimal, max value $10,000 |
| `zip` | Must match US ZIP format (##### or #####-####) |

## Business Rules

1. **Time Conflict Validation**: Appointments cannot overlap for the same provider (30-minute buffer)
2. **Future Date Validation**: Appointment time must be at least 1 hour in the future
3. **Active User Validation**: Both patient and provider must be active in the system
4. **Default Status**: All new appointments default to `SCHEDULED` status

## Error Responses

### 400 Bad Request
- Invalid input format
- Missing required fields
- Validation failures

### 409 Conflict
- Appointment time conflicts with existing appointment for the same provider

### 422 Unprocessable Entity
- Business rule violations
- Invalid patient/provider IDs

### 500 Internal Server Error
- Database or system errors

## Database Schema

### Appointment Entity

```java
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    private UUID id;
    
    @ManyToOne
    private Patient patient;
    
    @ManyToOne
    private Provider provider;
    
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType; // NEW, FOLLOW_UP
    
    @Enumerated(EnumType.STRING)
    private AppointmentMode mode; // IN_PERSON, VIDEO_CALL, HOME
    
    private LocalDateTime dateTime;
    
    @Column(length = 250)
    private String reasonForVisit;
    
    private BigDecimal estimatedAmount;
    
    @Embedded
    private ClinicAddress clinicAddress;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // SCHEDULED, CHECKED_IN, IN_EXAM, CANCELLED
    
    private Boolean isActive = true;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

## Security

- JWT Authentication required
- Patient and provider IDs must correspond to valid and active users
- All endpoints are secured with Spring Security

## Testing

Run the tests with:

```bash
mvn test
```

The test suite includes:
- Successful appointment booking
- Invalid patient/provider ID validation
- Past date validation
- ZIP code format validation
- Amount limit validation
- Time conflict validation

## Swagger Documentation

Access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Project Structure

```
src/main/java/com/thinkitive/thinkemr/
├── controller/
│   └── AppointmentController.java
├── dto/
│   ├── AppointmentBookingRequest.java
│   └── AppointmentBookingResponse.java
├── entity/
│   ├── Appointment.java
│   ├── AppointmentType.java
│   ├── AppointmentMode.java
│   └── AppointmentStatus.java
├── repository/
│   └── AppointmentRepository.java
├── service/
│   ├── AppointmentService.java
│   └── impl/
│       └── AppointmentServiceImpl.java
└── util/
    ├── FutureAppointmentTime.java
    └── AppointmentTimeValidator.java
```

## Dependencies

The project includes all necessary dependencies in `pom.xml`:
- Spring Boot Starters (Web, Data JPA, Security)
- PostgreSQL driver
- JWT libraries
- SpringDoc OpenAPI
- Lombok
- Validation API

## Running the Application

1. Ensure PostgreSQL 15.x is running
2. Configure database connection in `application.yml`
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the API at `http://localhost:8080/api/v1/appointments/book`

## Example Usage

```bash
curl -X POST http://localhost:8080/api/v1/appointments/book \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "patient_id": "123e4567-e89b-12d3-a456-426614174000",
    "provider_id": "456e7890-e89b-12d3-a456-426614174001",
    "appointment_type": "NEW",
    "mode": "IN_PERSON",
    "date_time": "2024-01-15T10:00:00",
    "reason_for_visit": "Annual checkup and blood work",
    "estimated_amount": 150.00,
    "clinic_address": {
      "street": "123 Main Street",
      "city": "Boston",
      "state": "MA",
      "zip": "02101"
    }
  }'
``` 