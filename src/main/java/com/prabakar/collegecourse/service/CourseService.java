package com.prabakar.collegecourse.service;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.exception.CourseNotFoundException;
import com.prabakar.collegecourse.exception.DuplicateCourseException;
import com.prabakar.collegecourse.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course addCourse(Course course) {
        boolean alreadyExists =
                repository.findByCode(course.getCode()).isPresent();

        if (alreadyExists) {
            throw new DuplicateCourseException(course.getCode());
        }

        return repository.save(course);
    }

    public Course getCourse(String code) {
        return repository.findByCode(code)
                .orElseThrow(() ->
                        new CourseNotFoundException(code));
    }

    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    public void deleteCourse(String code) {
        boolean deleted = repository.deleteByCode(code);

        if (!deleted) {
            throw new CourseNotFoundException(code);
        }
    }
}