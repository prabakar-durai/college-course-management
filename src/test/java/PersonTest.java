import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void studentShouldInheritFromPerson() {
        Student student = new Student(
                1L,
                "Arun",
                "arun@example.com",
                "MCA2026001"
        );

        assertInstanceOf(Person.class, student);
        assertEquals("Arun", student.getName());
        assertEquals("Student", student.getRole());
    }

    @Test
    void facultyShouldInheritFromPerson() {
        Faculty faculty = new Faculty(
                2L,
                "Prabakar",
                "prabakar@example.com",
                "FAC1001",
                "Assistant Professor"
        );

        assertInstanceOf(Person.class, faculty);
        assertEquals("Prabakar", faculty.getName());
        assertEquals("Faculty", faculty.getRole());
    }

    @Test
    void shouldDemonstrateRuntimePolymorphism() {
        Person student = new Student(
                1L,
                "Arun",
                "arun@example.com",
                "MCA2026001"
        );

        Person faculty = new Faculty(
                2L,
                "Prabakar",
                "prabakar@example.com",
                "FAC1001",
                "Assistant Professor"
        );

        List<Person> people = List.of(student, faculty);

        List<String> roles = people.stream()
                .map(Person::getRole)
                .toList();

        assertEquals(
                List.of("Student", "Faculty"),
                roles
        );
    }
}