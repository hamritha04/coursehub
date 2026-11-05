package com.coursehub.dto;

import com.coursehub.model.Student;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static Student toEntity(StudentRequest request) {
        Student student = new Student();
        student.setName(request.name());
        student.setEmail(request.email());
        student.setDepartment(request.department());
        student.setYearOfStudy(request.yearOfStudy());
        return student;
    }

    public static void updateEntity(Student student, StudentRequest request) {
        student.setName(request.name());
        student.setEmail(request.email());
        student.setDepartment(request.department());
        student.setYearOfStudy(request.yearOfStudy());
    }

    public static StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getDepartment(),
                student.getYearOfStudy()
        );
    }
}