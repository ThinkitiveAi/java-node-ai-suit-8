package com.thinkitive.thinkemr.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AppointmentTimeValidator implements ConstraintValidator<FutureAppointmentTime, LocalDateTime> {
    
    @Override
    public void initialize(FutureAppointmentTime constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation
        }
        
        // Check if the appointment time is at least 1 hour in the future
        LocalDateTime minimumAppointmentTime = LocalDateTime.now().plusHours(1);
        return value.isAfter(minimumAppointmentTime);
    }
} 