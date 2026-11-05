package com.coursehub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(

        @NotBlank(message = "Student name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        String email,

        @NotBlank(message = "Department is required")
        String department,

        @NotNull(message = "Year of study is required")
        @Min(value = 1, message = "Year must be at least 1")
        @Max(value = 4, message = "Year cannot exceed 4")
        Integer yearOfStudy

) {
}