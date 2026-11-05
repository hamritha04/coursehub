package com.coursehub.controller;

import com.coursehub.repository.CourseRepository;
import com.coursehub.repository.RegistrationRepository;
import com.coursehub.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;

    public DashboardController(
            CourseRepository courseRepository,
            StudentRepository studentRepository,
            RegistrationRepository registrationRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", courseRepository.count());
        model.addAttribute("totalStudents", studentRepository.count());
        model.addAttribute("totalRegistrations",
                registrationRepository.countByStatus("ACTIVE"));

        return "dashboard";
    }
}