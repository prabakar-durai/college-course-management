package com.prabakar.collegecourse.repository;

import com.prabakar.collegecourse.domain.Course;
import com.prabakar.collegecourse.repository.CourseRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.Locale;

@Repository
public class InMemoryCourseRepository implements CourseRepository {

    private final Map<String, Course> courses = new LinkedHashMap<>();

    @Override
    public Course save(Course course) {
        courses.put(course.getCode(), course);
        return course;
    }

    @Override
    public Optional<Course> findByCode(String code) {
        return Optional.ofNullable(courses.get(code));
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public boolean deleteByCode(String code) {
        return courses.remove(code) != null;
    }

    @Override
    public Course update(Course course) {
        courses.put(course.getCode(), course);
        return course;
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return createPage(
                new ArrayList<>(courses.values()),
                pageable
        );
    }

    @Override
    public Page<Course> search(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return findAll(pageable);
        }

        String normalizedQuery = query.toLowerCase(Locale.ROOT);

        List<Course> matchingCourses = courses.values()
                .stream()
                .filter(course ->
                        course.getCode()
                                .toLowerCase(Locale.ROOT)
                                .contains(normalizedQuery)
                                ||
                                course.getTitle()
                                        .toLowerCase(Locale.ROOT)
                                        .contains(normalizedQuery)
                )
                .toList();

        return createPage(matchingCourses, pageable);
    }

    private Page<Course> createPage(
            List<Course> source,
            Pageable pageable) {

        List<Course> sortedCourses = new ArrayList<>(source);

        Comparator<Course> comparator = pageable.getSort()
                .stream()
                .map(this::createComparator)
                .reduce(Comparator::thenComparing)
                .orElse(null);

        if (comparator != null) {
            sortedCourses.sort(comparator);
        }

        int start = Math.toIntExact(pageable.getOffset());

        if (start >= sortedCourses.size()) {
            return new PageImpl<>(
                    List.of(),
                    pageable,
                    sortedCourses.size()
            );
        }

        int end = Math.min(
                start + pageable.getPageSize(),
                sortedCourses.size()
        );

        return new PageImpl<>(
                sortedCourses.subList(start, end),
                pageable,
                sortedCourses.size()
        );
    }

    private Comparator<Course> createComparator(Sort.Order order) {
        Comparator<Course> comparator = switch (order.getProperty()) {
            case "id" -> Comparator.comparing(Course::getId);
            case "code" -> Comparator.comparing(
                    Course::getCode,
                    String.CASE_INSENSITIVE_ORDER
            );
            case "title" -> Comparator.comparing(
                    Course::getTitle,
                    String.CASE_INSENSITIVE_ORDER
            );
            default -> throw new IllegalArgumentException(
                    "Unsupported course sort property: "
                            + order.getProperty()
            );
        };

        return order.isDescending()
                ? comparator.reversed()
                : comparator;
    }
}