package com.thinkitive.thinkemr.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AppointmentTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureAppointmentTime {
    String message() default "Appointment time must be at least 1 hour in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 