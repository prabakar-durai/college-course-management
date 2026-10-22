package com.prabakar.collegecourse.domain;

import com.prabakar.collegecourse.domain.Course;

import java.util.List;

public interface Teachable {
    void assignCourse(Course course);
    List<Course> getAssignedCourses();
}
