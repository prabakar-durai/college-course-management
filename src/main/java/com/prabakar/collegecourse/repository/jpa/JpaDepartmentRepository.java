package com.prabakar.collegecourse.repository.jpa;

import com.prabakar.collegecourse.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaDepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);
}