package com.coursehub.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegistrationRequest(

        @NotNull(message = "Student ID is required")
        @Positive(message = "Student ID must be positive")
        Long studentId,

        @NotNull(message = "Course ID is required")
        @Positive(message = "Course ID must be positive")
        Long courseId

) {
}