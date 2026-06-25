# 🌐 Network Community

A backend social network project built with **Spring Boot** following a layered architecture and real-world development practices.  
This project was developed as part of a mentorship training focused on backend engineering, database integration, and professional Git workflow using GitHub Issues and feature branches.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA (Hibernate)
- Spring Security (base configuration)
- Thymeleaf
- PostgreSQL
- Maven

---

## 🧠 Architecture

The project follows a **Layered Architecture (MVC Pattern)**, separating responsibilities to ensure clean code, scalability, and maintainability:

### 📌 Layers

- **Controller Layer** → Handles HTTP requests and responses
- **Service Layer** → Contains business rules and validation
- **Repository Layer** → Manages database communication using JPA
- **Entity Layer** → Defines the domain models mapped to the database

---

## 🧩 Key Concepts Applied

- RESTful-style endpoints (basic MVC flow)
- ORM with JPA/Hibernate
- Dependency Injection (Spring IoC)
- Form binding with Thymeleaf
- PostgreSQL relational database integration
- Exception handling basics
- GitHub Issues workflow for task management
- Feature-based development using Git branches

---

## 🗄️ Database

- PostgreSQL used as the main database
- Automatic schema generation via Hibernate (`ddl-auto=update`)
- User table includes:

  - id
  - name
  - email (unique)
  - password
  - bio
  - profile_image_url
  - created_at

---

## 🎯 Project Goals

This project was built to simulate a real backend environment and improve skills in:

- Spring Boot backend development
- Clean architecture design
- Database modeling and integration
- Version control with Git & GitHub
- Task organization using Issues
- Professional development workflow

---

## 🚀 Future Improvements

- Spring Security authentication (login system)
- Password encryption with BCrypt
- JWT authentication
- User profile management
- Social feed (posts system)
- REST API separation (frontend/backend decoupling)

---

## 👨‍💻 Author

Developed by **Gilvan Menezes**  
Backend development mentorship project focused on real-world engineering practices.
