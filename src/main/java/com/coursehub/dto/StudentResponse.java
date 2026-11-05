package com.coursehub.dto;

public record StudentResponse(
        Long id,
        String name,
        String email,
        String department,
        int yearOfStudy
) {
}