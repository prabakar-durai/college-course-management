package com.prabakar.collegecourse.repository;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import com.prabakar.collegecourse.repository.CourseRepository;
import com.prabakar.collegecourse.repository.InMemoryCourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Repository
class InMemoryCourseRepositoryTest {

    private CourseRepository repository;
    private Course javaCourse;

    @BeforeEach
    void setUp() {
        Department department =
                new Department(1L, "Computer Applications", "MCA");

        javaCourse = new Course(
                101L,
                "JAVA101",
                "Java Programming",
                department
        );

        repository = new InMemoryCourseRepository();
    }

    @Test
    void shouldSaveAndFindCourse() {
        repository.save(javaCourse);

        Optional<Course> result =
                repository.findByCode("JAVA101");

        assertTrue(result.isPresent());
        assertEquals(javaCourse, result.get());
    }

    @Test
    void shouldReturnEmptyWhenCourseDoesNotExist() {
        Optional<Course> result =
                repository.findByCode("UNKNOWN");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllCourses() {
        repository.save(javaCourse);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldDeleteCourse() {
        repository.save(javaCourse);

        boolean deleted =
                repository.deleteByCode("JAVA101");

        assertTrue(deleted);
        assertTrue(repository.findByCode("JAVA101").isEmpty());
    }
}