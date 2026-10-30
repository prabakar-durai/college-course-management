package com.prabakar.collegecourse.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.prabakar.collegecourse.repository.jpa.JpaCourseRepository;
import com.prabakar.collegecourse.repository.jpa.JpaDepartmentRepository;
import org.junit.jupiter.api.AfterEach;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaCourseRepository courseRepository;

    @Autowired
    private JpaDepartmentRepository departmentRepository;

    @AfterEach
    void cleanDatabase() {
        courseRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    private void createCourse(
            Long id,
            String code,
            String title) throws Exception {

        String request = """
            {
              "id": %d,
              "code": "%s",
              "title": "%s",
              "departmentId": 710,
              "departmentName": "Computer Applications",
              "departmentCode": "MCA-PAGE"
            }
            """.formatted(id, code, title);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCompleteCourseLifecycle() throws Exception {
        String createRequest = """
                {
                  "id": 7001,
                  "code": "INT101",
                  "title": "Integration Testing",
                  "departmentId": 700,
                  "departmentName": "Computer Applications",
                  "departmentCode": "MCA-INT"
                }
                """;

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7001))
                .andExpect(jsonPath("$.code").value("INT101"))
                .andExpect(jsonPath("$.title")
                        .value("Integration Testing"))
                .andExpect(jsonPath("$.department.code")
                        .value("MCA-INT"));

        mockMvc.perform(get("/api/courses/INT101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("INT101"))
                .andExpect(jsonPath("$.title")
                        .value("Integration Testing"));

        String updateRequest = """
                {
                  "id": 7001,
                  "title": "Advanced Integration Testing",
                  "departmentId": 700,
                  "departmentName": "Computer Applications",
                  "departmentCode": "MCA-INT"
                }
                """;

        mockMvc.perform(put("/api/courses/INT101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("INT101"))
                .andExpect(jsonPath("$.title")
                        .value("Advanced Integration Testing"));

        mockMvc.perform(get("/api/courses/INT101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Advanced Integration Testing"));

        mockMvc.perform(delete("/api/courses/INT101"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/courses/INT101"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPaginateSortAndSearchCourses() throws Exception {
        createCourse(
                7101L,
                "ALG101",
                "Algorithms"
        );

        createCourse(
                7102L,
                "JAVA201",
                "Java Programming"
        );

        createCourse(
                7103L,
                "WEB201",
                "Web Technologies"
        );

        mockMvc.perform(get("/api/courses")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "title,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title")
                        .value("Algorithms"))
                .andExpect(jsonPath("$.content[1].title")
                        .value("Java Programming"))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(3))
                .andExpect(jsonPath("$.page.totalPages").value(2));

        mockMvc.perform(get("/api/courses")
                        .param("query", "WEB")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].code")
                        .value("WEB201"))
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }
}