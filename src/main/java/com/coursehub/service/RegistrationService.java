package com.coursehub.service;

import com.coursehub.dto.RegistrationMapper;
import com.coursehub.dto.RegistrationRequest;
import com.coursehub.dto.RegistrationResponse;
import com.coursehub.exception.ResourceNotFoundException;
import com.coursehub.model.Course;
import com.coursehub.model.Registration;
import com.coursehub.model.Student;
import com.coursehub.repository.CourseRepository;
import com.coursehub.repository.RegistrationRepository;
import com.coursehub.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public RegistrationResponse registerStudent(RegistrationRequest request) {

        Long studentId = request.studentId();
        Long courseId = request.courseId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with ID: " + studentId));

        Course course = courseRepository.findByIdForUpdate(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with ID: " + courseId));

        Registration existing = registrationRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElse(null);

        if (existing != null && "ACTIVE".equals(existing.getStatus())) {
            throw new IllegalArgumentException(
                    "Student is already registered for this course");
        }

        if (course.getEnrolledStudents() >= course.getCapacity()) {
            throw new IllegalStateException(
                    "Course has reached maximum capacity");
        }

        if (existing != null) {
            existing.setStatus("ACTIVE");
            course.setEnrolledStudents(course.getEnrolledStudents() + 1);
            courseRepository.save(course);

            return RegistrationMapper.toResponse(
                    registrationRepository.save(existing));
        }

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setCourse(course);
        registration.setStatus("ACTIVE");

        course.setEnrolledStudents(course.getEnrolledStudents() + 1);
        courseRepository.save(course);

        return RegistrationMapper.toResponse(
                registrationRepository.save(registration));
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> getAllRegistrations() {
        return registrationRepository.findAll()
                .stream()
                .map(RegistrationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegistrationResponse getRegistrationById(Long id) {
        return RegistrationMapper.toResponse(findRegistration(id));
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> getRegistrationsByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException(
                    "Student not found with ID: " + studentId);
        }

        return registrationRepository.findByStudentId(studentId)
                .stream()
                .map(RegistrationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> getRegistrationsByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException(
                    "Course not found with ID: " + courseId);
        }

        return registrationRepository.findByCourseId(courseId)
                .stream()
                .map(RegistrationMapper::toResponse)
                .toList();
    }

    public RegistrationResponse cancelRegistration(Long registrationId) {
        Registration registration = findRegistration(registrationId);

        if ("CANCELLED".equals(registration.getStatus())) {
            throw new IllegalStateException(
                    "Registration is already cancelled");
        }

        Course course = registration.getCourse();

        registration.setStatus("CANCELLED");
        course.setEnrolledStudents(course.getEnrolledStudents() - 1);

        courseRepository.save(course);

        return RegistrationMapper.toResponse(
                registrationRepository.save(registration));
    }

    private Registration findRegistration(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registration not found with ID: " + id));
    }
}