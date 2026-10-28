package com.prabakar.collegecourse.service;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.exception.CourseNotFoundException;
import com.prabakar.collegecourse.exception.DuplicateCourseException;
import com.prabakar.collegecourse.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private static final Logger log =
            LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course addCourse(Course course) {
        boolean alreadyExists =
                repository.findByCode(course.getCode()).isPresent();

        if (alreadyExists) {
            log.warn(
                    "Course creation rejected because code already exists: code={}",
                    course.getCode()
            );

            throw new DuplicateCourseException(course.getCode());
        }

        Course savedCourse = repository.save(course);

        log.info(
                "Course created successfully: code={}",
                savedCourse.getCode()
        );

        return savedCourse;
    }

    public Course getCourse(String code) {
        log.debug("Retrieving course: code={}", code);

        return repository.findByCode(code)
                .orElseThrow(() -> {
                    log.warn("Course not found: code={}", code);
                    return new CourseNotFoundException(code);
                });
    }

    public List<Course> getAllCourses() {
        List<Course> courses = repository.findAll();

        log.debug(
                "Retrieved all courses: count={}",
                courses.size()
        );

        return courses;
    }

    public void deleteCourse(String code) {
        boolean deleted = repository.deleteByCode(code);

        if (!deleted) {
            log.warn(
                    "Course deletion failed because code was not found: code={}",
                    code
            );

            throw new CourseNotFoundException(code);
        }

        log.info("Course deleted successfully: code={}", code);
    }

    public Course updateCourse(
            String code,
            Course updatedCourse) {

        repository.findByCode(code)
                .orElseThrow(() -> {
                    log.warn(
                            "Course update failed because code was not found: code={}",
                            code
                    );

                    return new CourseNotFoundException(code);
                });

        Course savedCourse = repository.update(updatedCourse);

        log.info(
                "Course updated successfully: code={}",
                savedCourse.getCode()
        );

        return savedCourse;
    }
}