package com.coursehub.repository;

import com.coursehub.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByStudentId(Long studentId);

    List<Registration> findByCourseId(Long courseId);

    Optional<Registration> findByStudentIdAndCourseId(
            Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentId(Long studentId);

    long countByCourseIdAndStatus(Long courseId, String status);

    long countByStatus(String status);
}