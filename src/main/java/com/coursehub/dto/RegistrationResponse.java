package com.coursehub.dto;

import java.time.LocalDateTime;

public record RegistrationResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseName,
        LocalDateTime registrationDate,
        String status
) {
}