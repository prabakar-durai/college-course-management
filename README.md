# College Course Management API

A Spring Boot REST API for managing college courses and departments.

This project is being developed incrementally as a hands-on Java refresher and backend portfolio project. It demonstrates object-oriented design, layered architecture, REST API development, validation, exception handling, database persistence, and automated testing.

## Current Features

- Model departments, courses, students, and faculty members
- Associate courses with departments
- Create, retrieve, update, and delete courses through REST endpoints
- Persist courses and departments using Spring Data JPA and H2
- Retain database records across application restarts
- Reject duplicate course codes
- Report missing courses using custom exceptions
- Validate incoming API requests
- Return field-level validation errors
- Return appropriate HTTP status codes using centralized exception handling
- Demonstrate inheritance and interfaces through `Person`, `Student`, `Faculty`, and `Teachable`
- Verify domain, repository, service, controller, and persistence behaviour with automated tests

## REST API

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/courses` | Retrieve all courses |
| `GET` | `/api/courses/{code}` | Retrieve a course by code |
| `POST` | `/api/courses` | Create a course |
| `PUT` | `/api/courses/{code}` | Update a course |
| `DELETE` | `/api/courses/{code}` | Delete a course |

The API returns appropriate HTTP statuses, including:

- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`

## Example Course Request

```json
{
  "id": 101,
  "code": "JAVA101",
  "title": "Java Programming",
  "departmentId": 1,
  "departmentName": "Computer Applications",
  "departmentCode": "MCA"
}
```

## Technologies

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA
- Hibernate
- H2 Database
- Jakarta Bean Validation
- Maven
- JUnit
- Mockito
- IntelliJ IDEA
- Git and GitHub

## Architecture

The project follows a layered design:

```text
Controller
    ↓
Service
    ↓
Repository abstraction
    ↓
JPA repository adapter
    ↓
Spring Data JPA
    ↓
H2 database
```

The service layer depends on the `CourseRepository` abstraction rather than directly depending on Spring Data JPA. The JPA adapter implements this abstraction and handles database operations.

## Project Structure

```text
college-course-management/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/prabakar/collegecourse/
│   │   │   ├── controller/
│   │   │   ├── domain/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   │   └── jpa/
│   │   │   ├── service/
│   │   │   └── CollegeApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/prabakar/collegecourse/
└── .gitignore
```

## Running the Application

Open the project in IntelliJ IDEA and run:

```text
CollegeApplication
```

The application starts on:

```text
http://localhost:8080
```

To verify the API:

```bash
curl http://localhost:8080/api/courses
```

## Database

The application uses a file-based H2 database.

Database records are stored locally under the `data/` directory and remain available after application restarts. This directory is excluded from Git because it contains local runtime data.

### H2 Console

Start the application and open:

```text
http://localhost:8080/h2-console
```

Use the following connection details:

```text
JDBC URL: jdbc:h2:file:./data/college-course-db
User Name: sa
Password: leave blank
```

The database contains the following tables:

- `COURSES`
- `DEPARTMENTS`

The `COURSES.DEPARTMENT_ID` column associates each course with its department.

> The H2 console is intended only for local development and should not be enabled in production.
## Application Logging

The application uses SLF4J with Logback, provided by Spring Boot.

Logging is implemented in the service layer for meaningful business operations:

- `INFO` for successful course creation, update, and deletion
- `WARN` for duplicate courses and missing courses
- `DEBUG` for course retrieval details
- `ERROR` is reserved for unexpected system failures

Logs are written to both the console and `logs/college-course-management.log`.

Log files rotate when they reach 10 MB, and seven historical files are retained. The local `logs/` directory is excluded from Git.

The application package currently uses the `DEBUG` level for development. A production configuration should normally use `INFO` or `WARN`.

## Running the Tests

From IntelliJ IDEA, open the Maven tool window and run:

```text
Lifecycle → test
```

If Maven is installed and available from the terminal, run:

```bash
mvn test
```

Current test status: **37 tests passing**.

The test suite covers:

- Domain behaviour
- In-memory repository behaviour
- Service-layer operations
- REST controller endpoints
- Request validation
- Exception handling
- JPA repository adapter and H2 persistence

## API Documentation

Interactive OpenAPI documentation is available through Swagger UI while the application is running:

```text
http://localhost:8080/swagger-ui/index.html
```

The machine-readable OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI can be used to:

- View all available course endpoints
- Inspect request and response formats
- Review validation requirements
- Review possible HTTP response codes
- Execute API requests directly from the browser

## Roadmap

- Add full Spring Boot integration tests
- Introduce separate development and production configurations
- Replace H2 with PostgreSQL for production-style persistence
- Add pagination and course searching
- Add continuous integration with GitHub Actions
- Containerize the application using Docker

## Purpose

This project is being developed incrementally to refresh modern Java and backend-development skills while demonstrating clean code, automated testing, version control, REST API design, and database persistence.

## Author

Prabakar Duraisamyß