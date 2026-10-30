package com.prabakar.collegecourse.repository.jpa;

import com.prabakar.collegecourse.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCourseRepository
        extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);

    long deleteByCode(String code);

    Page<Course> findByCodeContainingIgnoreCaseOrTitleContainingIgnoreCase(
            String code,
            String title,
            Pageable pageable
    );
}