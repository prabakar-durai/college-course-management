package com.prabakar.collegecourse.dto;

public record CreateCourseRequest(
        Long id,
        String code,
        String title,
        Long departmentId,
        String departmentName,
        String departmentCode
) {
}