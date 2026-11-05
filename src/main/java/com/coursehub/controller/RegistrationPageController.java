package com.coursehub.controller;

import com.coursehub.dto.RegistrationRequest;
import com.coursehub.repository.CourseRepository;
import com.coursehub.repository.StudentRepository;
import com.coursehub.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registrations")
public class RegistrationPageController {

    private final RegistrationService registrationService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public RegistrationPageController(
            RegistrationService registrationService,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.registrationService = registrationService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String registrations(Model model) {
        model.addAttribute("registrations",
                registrationService.getAllRegistrations());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("registrationRequest",
                new RegistrationRequest(null, null));

        return "registrations";
    }

    @PostMapping
    public String registerStudent(
            @Valid @ModelAttribute("registrationRequest")
            RegistrationRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            loadPageData(model);
            return "registrations";
        }

        try {
            registrationService.registerStudent(request);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Student registered successfully!");
            return "redirect:/registrations";
        } catch (RuntimeException exception) {
            loadPageData(model);
            model.addAttribute("errorMessage", exception.getMessage());
            return "registrations";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelRegistration(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            registrationService.cancelRegistration(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Registration cancelled successfully!");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", exception.getMessage());
        }

        return "redirect:/registrations";
    }

    private void loadPageData(Model model) {
        model.addAttribute("registrations",
                registrationService.getAllRegistrations());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
    }
}