package com.prabakar.collegecourse.domain;

public class Student extends Person {

    private String registerNumber;

    public Student(Long id, String name, String email, String registerNumber) {
        super(id, name, email);
        this.registerNumber = registerNumber;
    }

    @Override
    public String getRole() {
        return "com.prabakar.collegecourse.domain.Student";
    }
}
