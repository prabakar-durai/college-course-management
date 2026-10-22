package com.prabakar.collegecourse.repository;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.repository.CourseRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCourseRepository implements CourseRepository {

    private final Map<String, Course> courses = new LinkedHashMap<>();

    @Override
    public Course save(Course course) {
        courses.put(course.getCode(), course);
        return course;
    }

    @Override
    public Optional<Course> findByCode(String code) {
        return Optional.ofNullable(courses.get(code));
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public boolean deleteByCode(String code) {
        return courses.remove(code) != null;
    }
}