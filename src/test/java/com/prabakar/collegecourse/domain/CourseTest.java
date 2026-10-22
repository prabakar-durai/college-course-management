package com.prabakar.collegecourse.domain;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Department mcaDepartment;
    private Department computerScience;
    private List<Course> courses;

    @BeforeEach
    void setUp() {
        mcaDepartment =
                new Department(1L, "Computer Applications", "MCA");

        computerScience =
                new Department(2L, "Computer Science", "CSC");

        courses = new ArrayList<>();

        courses.add(new Course(
                101L, "JAVA101", "Java Programming", mcaDepartment));

        courses.add(new Course(
                102L, "DBMS101", "Database Management Systems",
                mcaDepartment));

        courses.add(new Course(
                103L, "WEB101", "Web Technologies", mcaDepartment));

        courses.add(new Course(
                104L, "PY101", "Python Programming", computerScience));
    }

    @Test
    void shouldAssociateCourseWithDepartment() {
        Course javaCourse = courses.get(0);

        assertEquals("JAVA101", javaCourse.getCode());
        assertEquals("MCA", javaCourse.getDepartment().getCode());

        // Confirms it is the exact same com.prabakar.collegecourse.domain.Department object
        assertSame(mcaDepartment, javaCourse.getDepartment());
    }

    @Test
    void shouldFindCoursesByDepartmentCode() {
        List<Course> mcaCourses = courses.stream()
                .filter(course ->
                        course.getDepartment()
                                .getCode()
                                .equalsIgnoreCase("MCA"))
                .toList();

        assertEquals(3, mcaCourses.size());
        assertTrue(
                mcaCourses.stream()
                        .allMatch(course ->
                                course.getDepartment()
                                        .getCode()
                                        .equals("MCA"))
        );
    }

    @Test
    void shouldSortCourseTitlesAlphabetically() {
        List<String> sortedTitles = courses.stream()
                .sorted(Comparator.comparing(Course::getTitle))
                .map(Course::getTitle)
                .toList();

        assertEquals(
                List.of(
                        "Database Management Systems",
                        "Java Programming",
                        "Python Programming",
                        "Web Technologies"
                ),
                sortedTitles
        );
    }

    @Test
    void coursesWithSameCodeShouldBeEqual() {
        Course first = new Course(
                101L,
                "JAVA101",
                "Java Programming",
                mcaDepartment
        );

        Course second = new Course(
                101L,
                "JAVA101",
                "Java Programming",
                mcaDepartment
        );

        assertEquals(first, second);
    }

    @Test
    void shouldNotAddDuplicateCourseToSet() {
        Course first = new Course(
                101L, "JAVA101", "Java Programming", mcaDepartment);

        Course duplicate = new Course(
                101L, "JAVA101", "Java Programming", mcaDepartment);

        Set<Course> courses = new HashSet<>();

        courses.add(first);
        courses.add(duplicate);

        assertEquals(1, courses.size());
    }
}