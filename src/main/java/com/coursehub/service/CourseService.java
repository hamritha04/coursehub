package com.coursehub.service;

import com.coursehub.exception.ResourceNotFoundException;

import com.coursehub.dto.CourseMapper;
import com.coursehub.dto.CourseRequest;
import com.coursehub.dto.CourseResponse;
import com.coursehub.model.Course;
import com.coursehub.repository.CourseRepository;
import com.coursehub.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;

    public CourseService(
            CourseRepository courseRepository,
            RegistrationRepository registrationRepository) {
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
    }

    public CourseResponse addCourse(CourseRequest request) {
        Course course = CourseMapper.toEntity(request);
        return CourseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        return CourseMapper.toResponse(findCourse(id));
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course existing = findCourse(id);

        if (request.capacity() < existing.getEnrolledStudents()) {
            throw new IllegalArgumentException(
                    "Capacity cannot be less than current enrollment");
        }

        CourseMapper.updateEntity(existing, request);
        return CourseMapper.toResponse(courseRepository.save(existing));
    }

    public void deleteCourse(Long id) {
        Course course = findCourse(id);

        if (registrationRepository.countByCourseIdAndStatus(id, "ACTIVE") > 0) {
            throw new IllegalStateException(
                    "Cannot delete a course with active registrations");
        }

        courseRepository.delete(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByCategory(String category) {
        return courseRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(CourseMapper::toResponse)
                .toList();
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with ID: " + id));
    }
}