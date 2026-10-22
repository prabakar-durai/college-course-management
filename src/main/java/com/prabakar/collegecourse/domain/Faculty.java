package com.prabakar.collegecourse.domain;

import com.prabakar.collegecourse.domain.Course;

import java.util.ArrayList;
import java.util.List;

public class Faculty  extends Person implements Teachable {
    private String employeeNumber;
    private String designation;

    private List<Course> assignedCourses = new ArrayList<>();

    public Faculty(Long id, String name, String email, String employeeNumber, String designation) {
        super(id, name, email);
        this.employeeNumber = employeeNumber;
        this.designation = designation;
    }

    @Override
    public String getRole() {
        return "com.prabakar.collegecourse.domain.Faculty";
    }

    @Override
    public void assignCourse(Course course) {
        assignedCourses.add(course);

    }

    @Override
    public List<Course> getAssignedCourses() {
        return List.copyOf(assignedCourses);
    }
}
