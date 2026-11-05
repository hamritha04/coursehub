package com.coursehub.dto;

import com.coursehub.model.Course;

public final class CourseMapper {

    private CourseMapper() {
    }

    public static Course toEntity(CourseRequest request) {
        Course course = new Course();
        course.setName(request.name());
        course.setInstructor(request.instructor());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setCapacity(request.capacity());
        course.setEnrolledStudents(0);
        return course;
    }

    public static void updateEntity(Course course, CourseRequest request) {
        course.setName(request.name());
        course.setInstructor(request.instructor());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setCapacity(request.capacity());
    }

    public static CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getInstructor(),
                course.getDescription(),
                course.getCategory(),
                course.getCapacity(),
                course.getEnrolledStudents(),
                course.getCapacity() - course.getEnrolledStudents()
        );
    }
}