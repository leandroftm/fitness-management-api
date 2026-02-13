# 🏋️ Fitness Management API

RESTful API for managing a fitness center, built with Spring Boot.

---

## 📌 Overview

This project simulates a real-world backend system for a fitness center.  
It was designed with a strong focus on:

- Clean architecture principles
- Domain-driven design concepts
- Business rule enforcement
- Robust exception handling
- Integration testing
- Database versioning with Flyway

The goal is to reflect production-level backend standards.

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Security
- Bean Validation
- Flyway
- PostgreSQL
- H2 Database (development/testing)
- JUnit 5
- MockMvc (integration tests)

---

## 🏗 Architecture

The project follows a layered architecture:

Controller → Service → Repository → Database


### Domain Layer
- Entities
- Enums
- Domain Exceptions

### Application Layer
- DTOs
- Services
- Business validations

### Infrastructure Layer
- Database configuration
- External API integration (CEP client)
- Security configuration

---

## 🚀 Features

### 👤 Student Management
- Create student with address validation (ZIP Code integration)
- Prevent duplicated email
- Activate / Deactivate student
- Update student
- List only active students

### 🏋️ Exercise Management
- Create exercise
- Prevent duplicated exercise name
- Activate / Deactivate exercise
- Update exercise
- List only active exercises

### 📋 Training Program
- Create training program
- Prevent duplicated name
- Activate / Deactivate
- Update program

### 🔗 Training Program Exercises
- Add exercise to program
- Prevent duplicated exercise
- Prevent duplicated order
- Update exercise order
- Remove exercise
- Block operations on inactive entities

---

## 🧪 Testing Strategy

The project includes:

- Integration Tests with `@SpringBootTest`
- MockMvc for endpoint testing
- Database rollback using `@Transactional`
- Mocked external API (CEP client)
- Security disabled for test profile

All major business rules are covered by integration tests.

Run tests:

mvn clean test

---

## 🗄 Database Versioning

Flyway is used for database migrations.

Migration scripts are located in:

src/main/resources/db/migration


---

## ⚙️ Running the Application

### Using Maven:

mvn spring-boot:run


### Default Profiles

- Development → H2
- Production → PostgreSQL

---

## 🎯 Learning Goals

This project was built to simulate:

- Real-world API development
- Enterprise-level exception handling
- Clean separation of concerns
- Integration testing strategies
- Production-ready backend structure

---

## 👨‍💻 Author

Leandro Torres  
Backend Developer in training | Java & Spring Boot