package com.prabakar.collegecourse.repository.jpa;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaCourseRepositoryAdapter.class)
class JpaCourseRepositoryAdapterTest {

    @Autowired
    private JpaCourseRepositoryAdapter repository;

    private Course createCourse(String title) {
        Department department = new Department(
                1L,
                "Computer Applications",
                "MCA"
        );

        return new Course(
                101L,
                "JAVA101",
                title,
                department
        );
    }

    private Course createCourse(
            Long id,
            String code,
            String title) {

        Department department = new Department(
                1L,
                "Computer Applications",
                "MCA"
        );

        return new Course(
                id,
                code,
                title,
                department
        );
    }

    @Test
    void shouldSaveAndFindCourse() {
        repository.save(createCourse("Java Programming"));

        Course result = repository.findByCode("JAVA101")
                .orElseThrow();

        assertEquals("Java Programming", result.getTitle());
        assertEquals("MCA", result.getDepartment().getCode());
    }

    @Test
    void shouldUpdateCourse() {
        repository.save(createCourse("Java Programming"));

        repository.update(createCourse("Advanced Java"));

        Course result = repository.findByCode("JAVA101")
                .orElseThrow();

        assertEquals("Advanced Java", result.getTitle());
    }

    @Test
    void shouldDeleteCourse() {
        repository.save(createCourse("Java Programming"));

        assertTrue(repository.deleteByCode("JAVA101"));
        assertFalse(repository.findByCode("JAVA101").isPresent());
    }

    @Test
    void shouldReturnPaginatedCoursesSortedByTitle() {
        repository.save(createCourse(
                101L,
                "JAVA101",
                "Java Programming"
        ));

        repository.save(createCourse(
                102L,
                "DB101",
                "Database Systems"
        ));

        repository.save(createCourse(
                103L,
                "WEB101",
                "Web Technologies"
        ));

        Page<Course> result = repository.findAll(
                PageRequest.of(
                        0,
                        2,
                        Sort.by("title").ascending()
                )
        );

        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(
                "Database Systems",
                result.getContent().get(0).getTitle()
        );
        assertEquals(
                "Java Programming",
                result.getContent().get(1).getTitle()
        );
    }

    @Test
    void shouldSearchCodeAndTitleIgnoringCase() {
        repository.save(createCourse(
                101L,
                "JAVA101",
                "Java Programming"
        ));

        repository.save(createCourse(
                102L,
                "SPRING101",
                "Spring Boot Development"
        ));

        repository.save(createCourse(
                103L,
                "DB101",
                "Database Systems"
        ));

        Page<Course> codeResult = repository.search(
                "java",
                PageRequest.of(0, 10)
        );

        Page<Course> titleResult = repository.search(
                "BOOT",
                PageRequest.of(0, 10)
        );

        assertEquals(1, codeResult.getTotalElements());
        assertEquals(
                "JAVA101",
                codeResult.getContent().get(0).getCode()
        );

        assertEquals(1, titleResult.getTotalElements());
        assertEquals(
                "SPRING101",
                titleResult.getContent().get(0).getCode()
        );
    }
}