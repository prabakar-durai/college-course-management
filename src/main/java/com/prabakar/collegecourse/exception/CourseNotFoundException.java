package com.prabakar.collegecourse.exception;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String code) {
        super("com.prabakar.collegecourse.domain.Course not found: " + code);
    }
}