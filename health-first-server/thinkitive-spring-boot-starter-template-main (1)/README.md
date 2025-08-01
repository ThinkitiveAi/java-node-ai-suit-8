# ThinkEMR

A Minimal Spring Boot project for EMR backend APIs, featuring secure user management, validation, error handling, and environment-based configuration.

## Features
- Java 17, Spring Boot, Maven
- H2 in-memory database (for local development)
- User entity, repository, service, and REST controller
- Passwords are securely hashed (BCrypt)
- Request validation and global error handling
- Minimal dependencies, no external integrations
- Environment-based DB configuration
- Basic integration tests for user APIs

## Quick Start

```bash
# Clone the repo (replace with your repo URL after copying)
git clone <your-repo-url>
cd ThinkEMR

# Build and run
export JAVA_HOME=$(/usr/libexec/java_home)
mvn clean package
java -jar target/thinkemr-0.0.1-SNAPSHOT.jar
```

The app will start on [http://localhost:8765](http://localhost:8765)

## User Creation API

- **POST /api/users**
  - Create a new user
  - Sample request:

```bash
curl -X POST http://localhost:8765/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@thinkemr.com",
    "firstName": "John",
    "lastName": "Doe",
    "password": "secret"
  }'
```

- **GET /api/users/{id}**
  - Get user by ID

## How to Extend
- Add new entities in `entity/`
- Add new repositories in `repository/`
- Add new services in `service/`
- Add new controllers in `controller/`

## Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Lombok
- Spring Security (for password hashing)
- Jakarta Validation (for request validation)

---

**Security & Notes:**
- Passwords are hashed using BCrypt and never stored in plain text.
- Validation and error handling are enforced for all user input.
- Database configuration is loaded from environment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) with safe defaults for local development.
- The password field is returned in API responses for demo only; in production, remove it from responses.
- Basic integration tests are included for user creation and retrieval.

# Note
User creation and user get endpoints are provided to verify application setup, database configuration, and to test the GET API. These are for initial validation only. 