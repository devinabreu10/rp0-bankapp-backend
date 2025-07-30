# RP0 Banking Platform - Backend (API)

## Overview

The **RP0 Banking Platform** is a robust, enterprise-grade backend system for a digital banking platform. It provides secure RESTful APIs for managing customers, accounts, transactions, loans, and credit products. The application is built with modern Java, Spring Boot, and PostgreSQL, and is designed for scalability, maintainability, and security.

---

## Architecture

- **Spring Boot 3.3.x**: Core application framework
- **PostgreSQL**: Relational database for persistent storage
- **Spring Security & JWT**: Authentication and authorization
- **Caffeine Cache**: High-performance caching
- **Log4j2**: Structured logging
- **OpenAPI/Swagger**: API documentation
- **Docker & Docker Compose**: Containerized deployment

---

## Features

- **Customer Management**: Register, update, delete, and retrieve customer profiles
- **Account Management**: Open, view, and manage bank accounts
- **Transaction Management**: Record, view, and manage transactions
- **Loan & Credit Products**: Manage loans and credit cards
- **Funds Transfer**: Secure transfer of funds between accounts (with stored procedure)
- **Authentication**: Secure login with JWT tokens
- **API Documentation**: Interactive Swagger UI
- **Comprehensive Logging**
- **Unit & Integration Testing**

---

## Technology Stack

- Java 17
- Spring Boot 3.3.x
- Spring Security (JWT)
- JDBC
- PostgreSQL 15+
- Maven
- Docker
- Log4j2
- Caffeine Cache
- OpenAPI (springdoc)

---

## Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 15+ (if running outside Docker)

---

## Environment Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/devinabreu10/rp0-bankapp-backend.git
   cd rp0-bankapp-backend
   ```

2. **Environment Variables**
   Create a `.env` file or set the following environment variables:
   ```env
   DB_URL=jdbc:postgresql://localhost:5433/rp0-bankapp
   DB_USER=your_db_user
   DB_PASSWORD=your_db_password
   JWT_SECRET=your_jwt_secret
   CORS_ORIGINS=http://localhost:4200,http://localhost
   PORT=8080
   ```
   > If using Docker Compose, edit or create `.env/docker.env` accordingly.

---

## Database Setup

- The database schema is defined in [`src/main/resources/sql/finalized-pg-bankapp-schema.sql`](src/main/resources/sql/finalized-pg-bankapp-schema.sql).
- To initialize the database manually:
  ```bash
  psql -U <user> -d <database> -f src/main/resources/sql/finalized-pg-bankapp-schema.sql
  ```
- A stored procedure for secure account transfers is provided in [`src/main/resources/sql/transfer-account-funds-stored-procedure.sql`](src/main/resources/sql/transfer-account-funds-stored-procedure.sql).

---

## Build & Run

### Using Maven (Local Development)
```bash
mvn clean package
java -jar target/rp0-bankapp-backend-1.0.0.jar
```

### Using Shell Script
You can also start the application with the provided shell script, which loads environment variables and starts the JAR with GC logging:

```bash
./execute-rp0-bankapp.sh
```

This script expects a `.env/local.env` file with the required environment variables. It will fail if either the env file or built JAR is missing.

### Using Docker
Build and run the backend service:
```bash
docker build -t rp0-bankapp-api-local:latest .
docker run --env-file .env/docker.env -p 8088:8080 rp0-bankapp-api-local:latest
```

### Using Docker Compose (Recommended)
This will start PostgreSQL, the backend API, and (optionally) the UI:
```bash
docker-compose up --build
```

---

## API Overview

- **Base URL:** `http://localhost:8088/`
- **Swagger UI:** `http://localhost:8088/swagger-ui.html`

### Main Endpoints
- `/auth` - Authentication (login, get current user)
- `/customer` - Customer CRUD operations
- `/account` - Account CRUD and queries
- `/transaction` - Transaction CRUD and queries
- `/loan`, `/credit` - Loan and credit management

See Swagger UI for full API documentation and request/response schemas.

---

## Security

- JWT-based authentication for all protected endpoints
- Passwords are securely hashed
- CORS is configurable via environment variables
- Role-based access control (future enhancement)

---

## Logging

- Log4j2 is used for structured, high-performance logging
- Logs are written to `logs/` and console by default
- GC logs are enabled in Docker

---

## Testing

- Unit and integration tests are located in `src/test/java/dev/abreu/bankapp/`
- Run all tests:
  ```bash
  mvn test
  ```
- Code coverage is reported via JaCoCo

---

## Troubleshooting

- Ensure all environment variables are set correctly
- Check Docker container logs for errors:
  ```bash
  docker-compose logs
  ```
- Database connection issues: verify `DB_URL`, `DB_USER`, and `DB_PASSWORD`
- Port conflicts: ensure ports 8088 (API) and 5433 (Postgres) are available

---

## Contribution Guidelines

- Fork the repository and create a feature branch
- Write clear, descriptive commit messages
- Ensure all tests pass before submitting a PR
- Follow Java and Spring best practices
- Document new features in the README and Swagger

---

## License

This project is licensed under the MIT License. 