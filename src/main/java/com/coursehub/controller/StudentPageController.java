package com.coursehub.controller;

import com.coursehub.dto.StudentRequest;
import com.coursehub.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentPageController {

    private final StudentService studentService;

    public StudentPageController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String students(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("studentRequest",
                new StudentRequest("", "", "", 1));
        return "students";
    }

    @PostMapping
    public String addStudent(
            @Valid @ModelAttribute("studentRequest") StudentRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            return "students";
        }

        try {
            studentService.addStudent(request);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Student added successfully!");
            return "redirect:/students";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("errorMessage", exception.getMessage());
            return "students";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Student deleted successfully!");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", exception.getMessage());
        }

        return "redirect:/students";
    }
}