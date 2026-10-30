package com.prabakar.collegecourse.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    public Course(Long id, String code, String title, Department department) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.department = department;
    }

    protected Course(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "com.prabakar.collegecourse.domain.Course{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", department=" + department.getName() +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Course course)) {
            return false;
        }

        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
