package com.prabakar.collegecourse.controller;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prabakar.collegecourse.domain.Department;
import com.prabakar.collegecourse.dto.CreateCourseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{code}")
    public Course getCourse(@PathVariable String code) {
        return courseService.getCourse(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course addCourse(@RequestBody CreateCourseRequest request) {
        Department department = new Department(
                request.departmentId(),
                request.departmentName(),
                request.departmentCode()
        );

        Course course = new Course(
                request.id(),
                request.code(),
                request.title(),
                department
        );

        return courseService.addCourse(course);
    }
    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable String code) {
        courseService.deleteCourse(code);
    }
}