package com.coursehub.dto;

import com.coursehub.model.Registration;

public final class RegistrationMapper {

    private RegistrationMapper() {
    }

    public static RegistrationResponse toResponse(Registration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getStudent().getId(),
                registration.getStudent().getName(),
                registration.getCourse().getId(),
                registration.getCourse().getName(),
                registration.getRegistrationDate(),
                registration.getStatus()
        );
    }
}