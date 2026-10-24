package com.prabakar.collegecourse.repository.jpa;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.domain.Department;
import com.prabakar.collegecourse.repository.CourseRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Transactional
public class JpaCourseRepositoryAdapter
        implements CourseRepository {

    private final JpaCourseRepository courseRepository;
    private final JpaDepartmentRepository departmentRepository;

    public JpaCourseRepositoryAdapter(
            JpaCourseRepository courseRepository,
            JpaDepartmentRepository departmentRepository) {

        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Course save(Course course) {
        Department department = course.getDepartment();

        Department managedDepartment =
                departmentRepository.findById(department.getId())
                        .orElseGet(() ->
                                departmentRepository.save(department));

        course.setDepartment(managedDepartment);
        return courseRepository.save(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public boolean deleteByCode(String code) {
        if (!courseRepository.existsByCode(code)) {
            return false;
        }

        courseRepository.deleteByCode(code);
        return true;
    }

    @Override
    public Course update(Course course) {
        return save(course);
    }
}