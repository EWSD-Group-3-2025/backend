# E-Tutoring System (Spring Boot)

## Project Overview
E-Tutoring System is a Spring Boot-based application that facilitates online tutoring by providing role-based access, user management, and other features for tutors and students. This project is structured to follow best practices, ensuring maintainability and scalability.

## Features
- User authentication and authorization (JWT-based security)
- Role-based access control
- User and role management
- RESTful API with request/response handling
- Centralized configuration management
- Exception handling
- Database interactions with JPA
- Docker support for containerization in railway
- CI/CD pipeline with GitHub Actions

## Project Structure
```
backend:.
├───.github
│   └───workflows          # GitHub Actions workflows
├───.mvn                   # Maven wrapper files
├───src
│   ├───main
│   │   ├───java
│   │   │   └───org
│   │   │       └───teamSmurfs
│   │   │           └───backend
│   │   │               ├───actuator          # Spring Boot Actuator monitoring
│   │   │               ├───api               # API request and response handling
│   │   │               │   ├───request       # Request DTOs
│   │   │               │   ├───response      # Response DTOs and utilities
│   │   │               │   ├───role          # Role management
│   │   │               │   │   ├───dto       # Role DTOs
│   │   │               │   │   ├───model     # Role Models
│   │   │               │   │   ├───repository # Role Repositories
│   │   │               │   │   └───service   # Role Services
│   │   │               │   └───user          # User-related functionality
│   │   │               │       ├───controller   # REST Controllers
│   │   │               │       ├───dto           # Data Transfer Objects
│   │   │               │       ├───model         # Entity Models
│   │   │               │       ├───repository    # JPA Repositories
│   │   │               │       ├───service       # Service Layer
│   │   │               │       │   └───impl      # Service Implementations
│   │   │               │       └───utils         # Utility classes
│   │   │               ├───config            # Configuration files
│   │   │               │   ├───beans         # Bean configurations
│   │   │               │   ├───exception     # Global Exception handling
│   │   │               │   ├───properties    # Properties and environment settings
│   │   │               │   ├───service       # Config-related services
│   │   │               │   │   └───impl      # Service Implementations
│   │   │               │   └───utils         # Utility functions
│   │   │               ├───security          # Security configurations
│   │   │               │   ├───config        # Spring Security configuration
│   │   │               │   ├───controller    # Auth controllers
│   │   │               │   ├───dto           # Security DTOs
│   │   │               │   ├───interceptor   # Interceptor logic
│   │   │               │   ├───service       # Security-related services
│   │   │               │   │   └───impl      # Security Service Implementations
│   │   │               │   └───utils         # Security utilities
│   │   └───resources   # Application properties, SQL scripts, static files
│   │       ├───application.properties
│   │       ├───application-dev.properties
│   │       ├───application-prod.properties
│   └───test            # Unit and integration tests
├───.env                # File to store environment variables
├───Dockerfile          # Docker configuration for railway deployment
├───compose.yaml        # Docker Compose configuration for railway deployment
├───LICENSE             # Project License
├───mvnw / mvnw.cmd     # Maven Wrapper Scripts
├───pom.xml             # Project Object Model (POM) file
└───README.md           # Project documentation
```

## Getting Started
### Prerequisites
- Java 17+
- Maven 3.8+
- Docker (optional)

### Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/EWSD-Group-3-2025/backend.git
   ```
2. Navigate to the project directory:
   ```sh
   cd backend
   ```
3. Configure environment variables in `.env` file.
4. Build the project:
   ```sh
   mvn clean install
   ```
5. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## API Documentation
API documentation is available using Swagger.
- After starting the application, visit:
  ```
  http://localhost:3000/swagger-ui/
  ```

## License
This project is licensed under the MIT License - see the [`LICENSE`](./LICENSE) file for details.

## Contact

We'd love to hear from you! If you have any questions, suggestions, or feedback about this project, feel free to reach out.

- **Email**: [pthu1@kmd.edu.mm](mailto:pthu1@kmd.edu.mm)
