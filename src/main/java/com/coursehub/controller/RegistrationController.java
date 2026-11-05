package com.coursehub.controller;

import com.coursehub.dto.RegistrationRequest;
import com.coursehub.dto.RegistrationResponse;
import com.coursehub.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> registerStudent(
            @Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.registerStudent(request));
    }

    @GetMapping
    public ResponseEntity<List<RegistrationResponse>> getAllRegistrations() {
        return ResponseEntity.ok(registrationService.getAllRegistrations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationResponse> getRegistrationById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                registrationService.getRegistrationById(id));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List<RegistrationResponse>> getRegistrationsByStudent(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                registrationService.getRegistrationsByStudent(id));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<List<RegistrationResponse>> getRegistrationsByCourse(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                registrationService.getRegistrationsByCourse(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RegistrationResponse> cancelRegistration(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                registrationService.cancelRegistration(id));
    }
}