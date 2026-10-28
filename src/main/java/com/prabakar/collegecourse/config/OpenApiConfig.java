package com.prabakar.collegecourse.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "College Course Management API",
                version = "1.0.0",
                description = """
                        REST API for managing college courses and departments.
                        Supports course creation, retrieval, update, deletion,
                        validation, and persistent storage.
                        """,
                contact = @Contact(
                        name = "Prabakar Duraisamy",
                        url = "https://github.com/prabakar-durai/college-course-management"
                )
        )
)
public class OpenApiConfig {
}