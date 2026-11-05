package com.coursehub.service;

import com.coursehub.dto.StudentMapper;
import com.coursehub.dto.StudentRequest;
import com.coursehub.dto.StudentResponse;
import com.coursehub.exception.ResourceNotFoundException;
import com.coursehub.model.Student;
import com.coursehub.repository.RegistrationRepository;
import com.coursehub.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;

    public StudentService(
            StudentRepository studentRepository,
            RegistrationRepository registrationRepository) {
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
    }

    public StudentResponse addStudent(StudentRequest request) {
        if (studentRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException(
                    "A student with this email already exists");
        }

        Student student = StudentMapper.toEntity(request);
        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        return StudentMapper.toResponse(findStudent(id));
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student existing = findStudent(id);

        studentRepository.findByEmailIgnoreCase(request.email())
                .filter(student -> !student.getId().equals(id))
                .ifPresent(student -> {
                    throw new IllegalArgumentException(
                            "Another student is already using this email");
                });

        StudentMapper.updateEntity(existing, request);

        return StudentMapper.toResponse(studentRepository.save(existing));
    }

    public void deleteStudent(Long id) {
        Student student = findStudent(id);

        if (registrationRepository.existsByStudentId(id)) {
            throw new IllegalStateException(
                    "Cannot delete a student with registration history");
        }

        studentRepository.delete(student);
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with ID: " + id));
    }
}