package com.coursehub.controller;

import com.coursehub.dto.CourseRequest;
import com.coursehub.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CoursePageController {

    private final CourseService courseService;

    public CoursePageController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String courses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("courseRequest", new CourseRequest(
                "", "", "", "", 1));
        return "courses";
    }

    @PostMapping
    public String addCourse(
            @Valid @ModelAttribute("courseRequest") CourseRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            return "courses";
        }

        try {
            courseService.addCourse(request);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Course added successfully!");
            return "redirect:/courses";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("errorMessage", exception.getMessage());
            return "courses";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCourse(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Course deleted successfully!");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", exception.getMessage());
        }

        return "redirect:/courses";
    }
}