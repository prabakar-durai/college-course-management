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

## Technologies

- Java 21
- Maven
- JUnit 5
- IntelliJ IDEA
- Git and GitHub

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

Current test status: **22 tests passing**.

## Roadmap

- Organize classes into domain, repository, service, and exception packages
- Convert the project into a Spring Boot application
- Expose course and department operations through REST APIs
- Add request validation and consistent API error responses
- Add a relational database using Spring Data JPA
- Add integration tests and API documentation

## Purpose

This project is being developed incrementally to refresh modern Java and backend-development skills while demonstrating clean code, testing, version control, and API design.

## Author

Prabakar Duraisamy
