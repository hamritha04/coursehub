package com.coursehub.dto;

public record CourseResponse(
        Long id,
        String name,
        String instructor,
        String description,
        String category,
        int capacity,
        int enrolledStudents,
        int availableSeats
) {
}