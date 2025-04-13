# ServletsPOC

## Overview
ServletsPOC is a Java-based web application that demonstrates the use of Servlets, Hibernate, and a PostgreSQL database for managing user data. The application provides CRUD (Create, Read, Update, Delete) operations for users through RESTful endpoints.

## Features
- **User Management**: Create, read, update, and delete user records.
- **Validation**: Input validation using Jakarta Bean Validation.
- **JSON Support**: Uses Jackson for JSON serialization and deserialization.
- **Logging**: Configurable logging for debugging and monitoring.

## Technologies Used
- **Java**: Core programming language.
- **Jakarta Servlet API**: For handling HTTP requests and responses.
- **PostgreSQL**: Relational database for storing user data.
- **Jackson**: For JSON processing.
- **Maven**: Build and dependency management tool.
- **Docker**: For containerizing the application and database.

## Prerequisites
- Java 17 or higher
- Maven 3.8 or higher
- Docker and Docker Compose
- PostgreSQL (if not using Docker)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/renatompf/java_servlets_poc.git
cd java_servlets_poc
```

### 2. Run The Application
#### Using Docker
```bash
docker-compose up
```

#### Without Docker
1. Ensure PostgreSQL is running and create a database named `servletspoc`.
2. Deploy the WAR file to a Tomcat server.
3. Access the application at `http://localhost:8080/users`.

### 3. Endpoints

| HTTP Method | Endpoint         | Description                  |
|-------------|------------------|------------------------------|
| GET         | `/users`         | Fetch all users              |
| GET         | `/users/{id}`    | Fetch a user by ID           |
| POST        | `/users`         | Create a new user            |
| PUT         | `/users/{id}`    | Update an existing user      |
| DELETE      | `/users/{id}`    | Delete a user by ID          |

### Example JSON Payloads

(If using BRUNO you can find the request and response examples in the [http directory](http) .)

#### Create User (POST `/users`)
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "country": "USA"
}
```

#### Update User (PUT `/users/{id}`)
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "country": "USA"
}
```

