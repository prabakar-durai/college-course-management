# College com.prabakar.collegecourse.domain.Course Management

A Java-based college course management system built as a hands-on Java refresher project. The project currently focuses on object-oriented domain modelling, a simple repository/service design, exception handling, and automated unit testing.

## Current Features

- Model departments, courses, students, and faculty members
- Associate courses with departments
- Store courses using an in-memory repository
- Add, find, list, and remove courses through a service layer
- Reject duplicate courses
- Report missing courses using custom exceptions
- Demonstrate inheritance and interfaces through `com.prabakar.collegecourse.domain.Person`, `com.prabakar.collegecourse.domain.Student`, `com.prabakar.collegecourse.domain.Faculty`, and `com.prabakar.collegecourse.domain.Teachable`
- Verify behaviour with JUnit 5 tests

## REST API

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/courses` | Retrieve all courses |
| `GET` | `/api/courses/{code}` | Retrieve a course by code |
| `POST` | `/api/courses` | Create a course |
| `PUT` | `/api/courses/{code}` | Update a course |
| `DELETE` | `/api/courses/{code}` | Delete a course |

The API returns appropriate HTTP statuses including `201`, `204`, `400`, `404`, and `409`.

## Technologies

- Java 21
- Maven
- JUnit 5
- IntelliJ IDEA
- Git and GitHub
- Spring Boot 4.1.2
- Spring Web MVC
- Jakarta Bean Validation

## Project Structure

```text
college-course-management/
├── pom.xml
├── src/
│   ├── main/java/       # Application and domain classes
│   └── test/java/       # JUnit tests
└── .gitignore
```

The classes are currently in the default package. A structured package layout will be introduced in a future refactoring.

## Running the Tests

From IntelliJ IDEA, open the Maven tool window and run:

```text
Lifecycle → test
```

If Maven is installed and available from the terminal, run:

```bash
mvn test
```

Current test status: **34 tests passing**.

## Roadmap
- Add a relational database using Spring Data JPA
- Add integration tests and API documentation

## Purpose

This project is being developed incrementally to refresh modern Java and backend-development skills while demonstrating clean code, testing, version control, and API design.

## Author

Prabakar Duraisamy
