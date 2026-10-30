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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
    void shouldReturnPaginatedCourses() throws Exception {
        Page<Course> page = new PageImpl<>(
                List.of(createJavaCourse()),
                PageRequest.of(
                        0,
                        10,
                        Sort.by("code").ascending()
                ),
                1
        );

        when(courseService.getCourses(
                isNull(),
                any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code")
                        .value("JAVA101"))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Java Programming"))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1));
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
    @Test
    void shouldDeleteCourse() throws Exception {
        doNothing()
                .when(courseService)
                .deleteCourse("JAVA101");

        mockMvc.perform(delete("/api/courses/JAVA101"))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourse("JAVA101");
    }

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownCourse()
            throws Exception {

        doThrow(new CourseNotFoundException("PYTHON101"))
                .when(courseService)
                .deleteCourse("PYTHON101");

        mockMvc.perform(delete("/api/courses/PYTHON101"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title")
                        .value("Course not found"));
    }
    @Test
    void shouldRejectBlankCourseData() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "id": 101,
                              "code": "",
                              "title": "",
                              "departmentId": 1,
                              "departmentName": "",
                              "departmentCode": ""
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.code")
                        .value("must not be blank"))
                .andExpect(jsonPath("$.errors.title")
                        .value("must not be blank"))
                .andExpect(jsonPath("$.errors.departmentName")
                        .value("must not be blank"));

        verifyNoInteractions(courseService);
    }

    @Test
    void shouldUpdateCourse() throws Exception {
        Department department = new Department(
                1L,
                "Computer Applications",
                "MCA"
        );

        Course updatedCourse = new Course(
                101L,
                "JAVA101",
                "Advanced Java",
                department
        );

        when(courseService.updateCourse(
                eq("JAVA101"),
                any(Course.class)))
                .thenReturn(updatedCourse);

        mockMvc.perform(put("/api/courses/JAVA101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "id": 101,
                              "title": "Advanced Java",
                              "departmentId": 1,
                              "departmentName": "Computer Applications",
                              "departmentCode": "MCA"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("JAVA101"))
                .andExpect(jsonPath("$.title")
                        .value("Advanced Java"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingUnknownCourse()
            throws Exception {

        when(courseService.updateCourse(
                eq("PYTHON101"),
                any(Course.class)))
                .thenThrow(
                        new CourseNotFoundException("PYTHON101"));

        mockMvc.perform(put("/api/courses/PYTHON101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "id": 102,
                              "title": "Python Programming",
                              "departmentId": 1,
                              "departmentName": "Computer Applications",
                              "departmentCode": "MCA"
                            }
                            """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title")
                        .value("Course not found"));
    }

    @Test
    void shouldPassSearchAndPaginationParametersToService()
            throws Exception {

        Page<Course> page = new PageImpl<>(
                List.of(createJavaCourse()),
                PageRequest.of(
                        1,
                        5,
                        Sort.by("title").descending()
                ),
                6
        );

        when(courseService.getCourses(
                eq("java"),
                any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/courses")
                        .param("query", "java")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "title,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code")
                        .value("JAVA101"))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.size").value(5))
                .andExpect(jsonPath("$.page.totalElements").value(6))
                .andExpect(jsonPath("$.page.totalPages").value(2));

        verify(courseService).getCourses(
                eq("java"),
                argThat(pageable ->
                        pageable.getPageNumber() == 1
                                && pageable.getPageSize() == 5
                                && pageable.getSort()
                                .getOrderFor("title")
                                .isDescending()
                )
        );
    }
}