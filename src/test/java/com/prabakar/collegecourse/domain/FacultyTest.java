package com.prabakar.collegecourse.domain;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import com.prabakar.collegecourse.domain.Faculty;
import com.prabakar.collegecourse.domain.Teachable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacultyTest {

    private Faculty faculty;
    private Course javaCourse;
    private Course dbmsCourse;

    @BeforeEach
    void setUp() {
        Department department =
                new Department(1L, "Computer Applications", "MCA");

        faculty = new Faculty(
                2L,
                "Prabakar",
                "prabakar@example.com",
                "FAC1001",
                "Assistant Professor"
        );

        javaCourse = new Course(
                101L, "JAVA101", "Java Programming", department);

        dbmsCourse = new Course(
                102L, "DBMS101",
                "Database Management Systems", department);
    }

    @Test
    void shouldImplementTeachableInterface() {
        assertInstanceOf(Teachable.class, faculty);
    }

    @Test
    void shouldAssignCoursesToFaculty() {
        faculty.assignCourse(javaCourse);
        faculty.assignCourse(dbmsCourse);

        assertEquals(2, faculty.getAssignedCourses().size());
        assertTrue(faculty.getAssignedCourses().contains(javaCourse));
        assertTrue(faculty.getAssignedCourses().contains(dbmsCourse));
    }

    @Test
    void assignedCourseListShouldBeReadOnly() {
        faculty.assignCourse(javaCourse);

        assertThrows(
                UnsupportedOperationException.class,
                () -> faculty.getAssignedCourses().add(dbmsCourse)
        );
    }
}