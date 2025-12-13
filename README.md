# Task Manager API

A robust, production-grade RESTful API for managing tasks, designed to demonstrate modern backend development practices. Built with **Spring Boot 3.5** and **Java 21**, this project emphasizes clean code architecture, data integrity, and automated testing.

This repository serves as a reference implementation for a scalable backend service, featuring a Layered Architecture, DTO pattern usage, Global Exception Handling, and OpenAPI documentation.

## Technology Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3.5.8
* **Build Tool:** Maven
* **Database:** H2 In-Memory Database (configured for easy migration to PostgreSQL)
* **Testing:** JUnit 5, Mockito
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Utilities:** Lombok, Jakarta Validation

## Architecture

The project follows a strict **Layered Architecture** to ensure separation of concerns and maintainability.

1.  **API Layer (Controller):** Handles HTTP requests, parses JSON, and performs initial input validation.
2.  **Service Layer:** Encapsulates business logic and handles the mapping between Data Transfer Objects (DTOs) and Persistence Entities.
3.  **Persistence Layer (Repository):** Manages database interactions using Spring Data JPA.
4.  **Data Layer (Entity):** Represents the persistent data state.

### Design Patterns Used
* **DTO Pattern:** Java Records are used to decouple the internal database schema from the external API contract.
* **Global Exception Handling:** A centralized `@ControllerAdvice` component captures exceptions and translates them into standardized JSON error responses.
* **Dependency Injection:** Constructor-based injection is used throughout for immutability and easier testing.

## Key Features

* **CRUD Operations:** Full support for creating, reading, updating (via PUT), and deleting tasks.
* **Advanced Filtering:** Capability to query tasks based on specific statuses or keywords.
* **Input Validation:** Strict validation rules using Jakarta Validation (`@NotBlank`, `@FutureOrPresent`, `@Size`) to ensure data integrity.
* **Automated Documentation:** Live API documentation accessible via a browser.

## API Specification

| Method   | Endpoint                            | Description                                      |
|:---------|:------------------------------------|:-------------------------------------------------|
| `POST`   | `/api/tasks`                        | Create a new task                                |
| `GET`    | `/api/tasks`                        | Retrieve all tasks                               |
| `GET`    | `/api/tasks/{id}`                   | Retrieve a specific task by ID                   |
| `PUT`    | `/api/tasks/{id}`                   | Update an existing task                          |
| `DELETE` | `/api/tasks/{id}`                   | Remove a task                                    |
| `GET`    | `/api/tasks/search?status={status}` | Filter tasks by status (TODO, IN_PROGRESS, DONE) |

## Getting Started

### Prerequisites
* JDK 21 or higher
* Maven 3.6 or higher

### Installation & Run

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/stefanobini99/taskmanager.git](https://github.com/stefanobini99/taskmanager.git)
    cd taskmanager
    ```

2.  **Build the project**
    ```bash
    mvn clean install
    ```

3.  **Run the application**
    ```bash
    mvn spring-boot:run
    ```

The application will start on port `8080`.

### Accessing Documentation
Once the application is running, the interactive OpenAPI specification is available at:

**http://localhost:8080/swagger-ui/index.html**

## Configuration

The application is currently configured to use an in-memory H2 database for development convenience.

**File:** `src/main/resources/application.properties`

```properties
spring.application.name=taskmanager
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

To switch to a production database (e.g. PostgreSQL), update the spring.datasource properties in this file.

## Testing
The project includes a suite of unit tests focusing on the Service layer business logic.

**To execute the tests:**
```bash
    mvn test
```

## Future Roadmap

The current version focuses on core CRUD functionality. The next development phases will focus on transforming this into a secured, enterprise-ready application.

* **Security Implementation:** Integrate **Spring Security 6** with **JWT** (JSON Web Tokens) to implement stateless authentication and Role-Based Access Control (RBAC).
* **Production Database:** Migrate from H2 (in-memory) to **PostgreSQL** running via Docker Compose to ensure data persistence.
* **Advanced Search:** Replace basic filtering with **Spring Data JPA Specifications** to allow dynamic, multi-criteria search queries (e.g., filter by date range AND status).
* **CI/CD Pipeline:** Configure **GitHub Actions** to automatically run the test suite on every push and build a Docker image.