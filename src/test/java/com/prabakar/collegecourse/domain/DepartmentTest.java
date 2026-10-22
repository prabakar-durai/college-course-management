package com.prabakar.collegecourse.domain;

import com.prabakar.collegecourse.domain.Department;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    @Test
    void shouldCreateDepartment() {
        Department department =
                new Department(1L, "Computer Applications", "MCA");

        assertEquals(1L, department.getId());
        assertEquals("Computer Applications", department.getName());
        assertEquals("MCA", department.getCode());
    }

    @Test
    void shouldUpdateDepartmentName() {
        Department department =
                new Department(1L, "Computer Applications", "MCA");

        department.setName("com.prabakar.collegecourse.domain.Department of Computer Applications");

        assertEquals(
                "com.prabakar.collegecourse.domain.Department of Computer Applications",
                department.getName()
        );
    }

    @Test
    void shouldRejectBlankDepartmentName() {
        Department department =
                new Department(1L, "Computer Applications", "MCA");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> department.setName("")
        );
        assertEquals("com.prabakar.collegecourse.domain.Department name can not be empty", exception.getMessage());
    }
}