package com.prabakar.collegecourse.repository.jpa;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

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
}