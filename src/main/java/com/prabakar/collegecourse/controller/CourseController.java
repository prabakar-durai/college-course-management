package com.prabakar.collegecourse.controller;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import com.prabakar.collegecourse.dto.CreateCourseRequest;
import com.prabakar.collegecourse.dto.UpdateCourseRequest;
import com.prabakar.collegecourse.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(
        name = "Courses",
        description = "Operations for managing college courses"
)
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(
            summary = "Retrieve all courses",
            description = "Returns every course currently stored in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Courses retrieved successfully"
    )
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{code}")
    @Operation(
            summary = "Retrieve a course by code",
            description = "Returns the course matching the supplied business code"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Course retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course code was not found"
            )
    })
    public Course getCourse(
            @Parameter(
                    description = "Unique business code of the course",
                    example = "JAVA101"
            )
            @PathVariable String code) {

        return courseService.getCourse(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a course",
            description = "Creates a course and associates it with a department"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Course created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A course with the same code already exists"
            )
    })
    public Course addCourse(
            @Valid @RequestBody CreateCourseRequest request) {

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

    @PutMapping("/{code}")
    @Operation(
            summary = "Update a course",
            description = "Updates the course identified by the supplied code"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Course updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course code was not found"
            )
    })
    public Course updateCourse(
            @Parameter(
                    description = "Unique business code of the course",
                    example = "JAVA101"
            )
            @PathVariable String code,
            @Valid @RequestBody UpdateCourseRequest request) {

        Department department = new Department(
                request.departmentId(),
                request.departmentName(),
                request.departmentCode()
        );

        Course updatedCourse = new Course(
                request.id(),
                code,
                request.title(),
                department
        );

        return courseService.updateCourse(code, updatedCourse);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a course",
            description = "Deletes the course identified by the supplied code"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Course deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course code was not found"
            )
    })
    public void deleteCourse(
            @Parameter(
                    description = "Unique business code of the course",
                    example = "JAVA101"
            )
            @PathVariable String code) {

        courseService.deleteCourse(code);
    }
}