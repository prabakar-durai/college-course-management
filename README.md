# College Course Management API

[![Maven CI](https://github.com/prabakar-durai/college-course-management/actions/workflows/maven-ci.yml/badge.svg)](https://github.com/prabakar-durai/college-course-management/actions/workflows/maven-ci.yml)

A Spring Boot REST API for managing college courses and departments.

This project is being developed incrementally as a hands-on Java refresher and backend portfolio project. It demonstrates object-oriented design, layered architecture, REST API development, validation, exception handling, database persistence, and automated testing.

## Current Features

- Model departments, courses, students, and faculty members
- Associate courses with departments
- Create, retrieve, update, and delete courses through REST endpoints
- Persist courses and departments using Spring Data JPA with H2 and PostgreSQL
- Manage the production database schema using versioned Flyway migrations
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
- PostgreSQL
- Flyway
- Docker Compose
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
H2 / PostgreSQL
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
│   │       ├── db/migration/
│   │       │   └── V1__create_course_schema.sql
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/com/prabakar/collegecourse/
└── .gitignore
```

## Environment Profiles

Spring Boot profiles separate configuration for different environments.

| Profile | Configuration file | Purpose |
|---|---|---|
| Common | `application.properties` | Application name and shared JPA settings |
| `dev` | `application-dev.properties` | File-based H2 database, H2 console, debug logging and local log files |
| `test` | `application-test.properties` | Isolated in-memory H2 database for automated tests |
| `prod` | `application-prod.properties` | PostgreSQL database, production logging and disabled development tools |

### Development Profile

When running the application locally with H2, activate the `dev` profile.

In IntelliJ IDEA:

1. Open **Run → Edit Configurations**
2. Select `CollegeApplication`
3. Enter the following under **Program arguments**:

```text
--spring.profiles.active=dev
```

The startup log should confirm:

```text
The following 1 profile is active: "dev"
```

The development profile:

- Preserves local H2 data between restarts
- Enables the H2 browser console
- Enables application debug logging
- Writes logs to the local `logs/` directory
- Enables Swagger UI and OpenAPI documentation

### Test Profile

The integration test activates the `test` profile automatically.

The test profile:

- Uses an isolated in-memory H2 database
- Creates and drops its schema during testing
- Does not modify the local development database
- Disables the H2 browser console
- Reduces application logging during tests

### Production Profile

The `prod` profile uses PostgreSQL.

PostgreSQL runs locally through Docker Compose. Copy the environment-variable template before starting it:

```bash
cp .env.example .env
```

Update the password in `.env`, then start PostgreSQL:

```bash
docker compose up -d
```

Check that the container is healthy:

```bash
docker compose ps
```

In IntelliJ IDEA, activate the production profile with:

```text
--spring.profiles.active=prod
```

The production profile:

- Connects to PostgreSQL
- Reads database configuration from environment variables
- Uses `INFO` application logging
- Disables the H2 browser console
- Disables Swagger UI and OpenAPI endpoints
- Runs versioned Flyway database migrations
- Uses Hibernate to validate the migrated schema

To stop PostgreSQL while preserving its database volume:

```bash
docker compose down
```

To stop PostgreSQL and permanently remove its local database volume:

```bash
docker compose down -v
```

> `docker compose down -v` deletes the locally stored PostgreSQL data.

## Running the Application

Open the project in IntelliJ IDEA and run `CollegeApplication` with either the `dev` or `prod` profile enabled.

The application starts at:

```text
http://localhost:8080
```

To verify the API:

```bash
curl http://localhost:8080/api/courses
```

When the `dev` profile is active, development tools are available at:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

These development tools are disabled when the `prod` profile is active.

## Database

The application supports different databases for different environments.

| Environment | Database | Storage |
|---|---|---|
| Development | H2 | File-based and persistent |
| Test | H2 | In-memory and temporary |
| Production | PostgreSQL | Docker-managed persistent volume |

### Development H2 Database

Development records are stored under the local `data/` directory and remain available after application restarts. The directory is excluded from Git because it contains runtime data.

The H2 console is available at:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:file:./data/college-course-db
User Name: sa
Password: leave blank
```

### PostgreSQL Database

The production profile connects to the PostgreSQL container defined in `compose.yml`.

Local connection details are read from `.env`. The `.env` file is excluded from Git, while `.env.example` documents the required variables:

```text
POSTGRES_HOST
POSTGRES_PORT
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
```

PostgreSQL data is stored in the Docker named volume:

```text
college-course-management_postgres-data
```

Both databases contain:

- `courses`
- `departments`

The `courses.department_id` column associates each course with its department.

> H2 is intended for local development and testing. PostgreSQL represents the production database configuration.

## Database Migrations

The production PostgreSQL schema is managed with Flyway.

Migration scripts are stored under:

```text
src/main/resources/db/migration
```

The initial migration is:

```text
V1__create_course_schema.sql
```

It creates the `departments` and `courses` tables, their unique constraints, and the foreign-key relationship between courses and departments.

Flyway records applied migrations in the `flyway_schema_history` table. Previously applied migration files must not be modified. Future schema changes should be introduced through new versioned scripts, for example:

```text
V2__add_course_duration.sql
```

In the production profile, Hibernate uses `ddl-auto=validate`. Flyway manages schema changes, while Hibernate verifies that the database schema matches the JPA entity mappings.

## Application Logging

The application uses SLF4J with Logback, provided by Spring Boot.

Logging is implemented in the service layer for meaningful business operations:

- `INFO` for successful course creation, update, and deletion
- `WARN` for duplicate courses and missing courses
- `DEBUG` for course retrieval details
- `ERROR` is reserved for unexpected system failures

Logs are written to both the console and `logs/college-course-management.log`.

Log files rotate when they reach 10 MB, and seven historical files are retained. The local `logs/` directory is excluded from Git.

The development profile uses `DEBUG` application logging, while the production profile uses `INFO`.

## Running the Tests

From IntelliJ IDEA, open the Maven tool window and run:

```text
Lifecycle → test
```

If Maven is installed and available from the terminal, run:

```bash
mvn test
```

Current test status: **38 tests passing**.

The test suite covers:

- Domain behaviour
- In-memory repository behaviour
- Service-layer operations
- REST controller endpoints
- Request validation
- Exception handling
- JPA repository adapter and H2 persistence
- Full course lifecycle integration through Controller, Service, JPA, and H2

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

## Continuous Integration

GitHub Actions automatically builds the application and runs the complete test suite on every push and pull request.

The CI pipeline:

- Uses an Ubuntu runner
- Installs Java 21
- Caches Maven dependencies
- Runs the Maven `verify` lifecycle
- Executes all 38 automated tests
- Fails the build if compilation or any test fails

## Roadmap

- Add pagination and course searching
- Containerize the Spring Boot application using Docker

## Purpose

This project is being developed incrementally to refresh modern Java and backend-development skills while demonstrating clean code, automated testing, version control, REST API design, and database persistence.

## Author

Prabakar Duraisamy
