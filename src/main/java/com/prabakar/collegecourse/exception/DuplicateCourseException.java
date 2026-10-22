package com.prabakar.collegecourse.exception;

public class DuplicateCourseException extends RuntimeException {

    public DuplicateCourseException(String code) {
        super("com.prabakar.collegecourse.domain.Course already exists: " + code);
    }
}