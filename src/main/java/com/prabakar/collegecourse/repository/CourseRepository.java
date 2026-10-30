package com.prabakar.collegecourse.repository;

import com.prabakar.collegecourse.domain.Course;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepository {

    Course save(Course course);

    Optional<Course> findByCode(String code);

    List<Course> findAll();

    boolean deleteByCode(String code);

    Course update(Course course);

    Page<Course> findAll(Pageable pageable);

    Page<Course> search(String query, Pageable pageable);
}