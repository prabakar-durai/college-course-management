package com.prabakar.collegecourse.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
}