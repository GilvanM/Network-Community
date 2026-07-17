# Network Community

A simple social network built with Spring Boot and Thymeleaf.

## Technologies

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- Maven

## Prerequisites

Before running the project, make sure you have installed:

- Java 21
- Maven
- PostgreSQL

## Clone the project

git clone <repository-url>
cd network-community

## Configure the database

Update the application.properties file with your PostgreSQL credentials.

Example:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/networkcommunity
spring.datasource.username=postgres
spring.datasource.password=your_password

## Run the application

Using Maven Wrapper:

./mvnw spring-boot:run

Or using Maven:

mvn spring-boot:run

## Access the application

http://localhost:8080