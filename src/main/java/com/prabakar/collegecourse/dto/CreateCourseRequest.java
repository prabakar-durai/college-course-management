package com.prabakar.collegecourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(

        @NotNull
        Long id,

        @NotBlank
        String code,

        @NotBlank
        String title,

        @NotNull
        Long departmentId,

        @NotBlank
        String departmentName,

        @NotBlank
        String departmentCode
) {
}