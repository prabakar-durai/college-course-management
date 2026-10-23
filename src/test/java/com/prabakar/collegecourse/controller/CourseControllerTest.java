package com.prabakar.collegecourse.controller;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import com.prabakar.collegecourse.exception.CourseNotFoundException;
import com.prabakar.collegecourse.exception.DuplicateCourseException;
import com.prabakar.collegecourse.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    private Course createJavaCourse() {
        Department department = new Department(
                1L,
                "Computer Applications",
                "MCA"
        );

        return new Course(
                101L,
                "JAVA101",
                "Java Programming",
                department
        );
    }

    @Test
    void shouldReturnAllCourses() throws Exception {
        when(courseService.getAllCourses())
                .thenReturn(List.of(createJavaCourse()));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("JAVA101"))
                .andExpect(jsonPath("$[0].title")
                        .value("Java Programming"));
    }

    @Test
    void shouldCreateCourse() throws Exception {
        when(courseService.addCourse(any(Course.class)))
                .thenReturn(createJavaCourse());

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 101,
                                  "code": "JAVA101",
                                  "title": "Java Programming",
                                  "departmentId": 1,
                                  "departmentName": "Computer Applications",
                                  "departmentCode": "MCA"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("JAVA101"));
    }

    @Test
    void shouldReturnNotFoundForUnknownCourse() throws Exception {
        when(courseService.getCourse("PYTHON101"))
                .thenThrow(new CourseNotFoundException("PYTHON101"));

        mockMvc.perform(get("/api/courses/PYTHON101"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title")
                        .value("Course not found"));
    }

    @Test
    void shouldReturnConflictForDuplicateCourse() throws Exception {
        when(courseService.addCourse(any(Course.class)))
                .thenThrow(new DuplicateCourseException("JAVA101"));

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 101,
                                  "code": "JAVA101",
                                  "title": "Java Programming",
                                  "departmentId": 1,
                                  "departmentName": "Computer Applications",
                                  "departmentCode": "MCA"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title")
                        .value("Duplicate course"));
    }
}