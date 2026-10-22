import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private CourseService service;
    private Course javaCourse;

    @BeforeEach
    void setUp() {
        CourseRepository repository =
                new InMemoryCourseRepository();

        service = new CourseService(repository);

        Department department =
                new Department(1L, "Computer Applications", "MCA");

        javaCourse = new Course(
                101L,
                "JAVA101",
                "Java Programming",
                department
        );
    }

    @Test
    void shouldAddAndRetrieveCourse() {
        service.addCourse(javaCourse);

        Course result = service.getCourse("JAVA101");

        assertEquals(javaCourse, result);
    }

    @Test
    void shouldRejectDuplicateCourseCode() {
        service.addCourse(javaCourse);

        DuplicateCourseException exception = assertThrows(
                DuplicateCourseException.class,
                () -> service.addCourse(javaCourse)
        );

        assertEquals(
                "Course already exists: JAVA101",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenCourseIsAbsent() {
        assertThrows(
                CourseNotFoundException.class,
                () -> service.getCourse("UNKNOWN")
        );
    }

    @Test
    void shouldDeleteCourse() {
        service.addCourse(javaCourse);

        service.deleteCourse("JAVA101");

        assertThrows(
                CourseNotFoundException.class,
                () -> service.getCourse("JAVA101")
        );
    }
}