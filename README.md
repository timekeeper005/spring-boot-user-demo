# Maven demo project for user registration, login and profile display

This project is a lightweight Maven + Spring Boot example that implements a complete user flow:

- user registration
- user login
- user profile retrieval
- JSON RESTful API design
- CORS configuration to support browser-based access

It is designed to be easy to read and easy to expand, and can later be replaced with MyBatis or other persistent storage without changing the controller interface.

## 1. Project structure

```text
spring-boot-user-demo
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.demo
│   │   │       ├── DemoApplication.java
│   │   │       ├── config
│   │   │       │   └── CorsConfig.java
│   │   │       ├── controller
│   │   │       │   └── UserController.java
│   │   │       ├── dto
│   │   │       │   ├── LoginRequest.java
│   │   │       │   ├── RegisterRequest.java
│   │   │       │   └── UserResponse.java
│   │   │       ├── model
│   │   │       │   └── User.java
│   │   │       └── service
│   │   │           └── UserService.java
│   │   └── resources
│   │       └── application.properties
│   └── test
└── .gitignore
```

## 2. Technology stack

- Java 17
- Maven 3.x
- Spring Boot 3.3.x
- Spring Web MVC
- JSON response
- CORS cross-domain configuration

## 3. Quick start

1. Go to the project root directory.
2. Run:

```bash
mvn clean package
```

3. Start the project:

```bash
java -jar target/spring-boot-user-demo-0.0.1-SNAPSHOT.jar
```

4. Visit the API:

- http://localhost:8080/api/users/register
- http://localhost:8080/api/users/login
- http://localhost:8080/api/users/{id}

## 4. API examples

### Register user

Request:

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "123456",
    "name": "Alice",
    "email": "alice@example.com",
    "phone": "13800000000"
  }'
```

Success response:

```json
{
  "code": 200,
  "message": "User registered successfully.",
  "data": {
    "id": "...",
    "username": "alice",
    "name": "Alice",
    "email": "alice@example.com",
    "phone": "13800000000",
    "createdAt": "2026-10-28T10:00:00",
    "updatedAt": "2026-10-28T10:00:00"
  }
}
```

### Login

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "123456"
  }'
```

Success response:

```json
{
  "code": 200,
  "message": "Login successful.",
  "data": {
    "token": "mock-token-xxx",
    "user": {
      "id": "...",
      "username": "alice",
      "name": "Alice",
      "email": "alice@example.com",
      "phone": "13800000000",
      "createdAt": "2026-10-28T10:00:00",
      "updatedAt": "2026-10-28T10:00:00"
    }
  }
}
```

### Get profile

```bash
curl http://localhost:8080/api/users/{id}
```

## 5. Maven lifecycle logic

Maven is a build tool based on a standard lifecycle. The most common phases are:

```text
validate -> compile -> test -> package -> verify -> install -> deploy
```

The core logical flow is:

1. `validate`
   - ensure project metadata and configuration are valid.
2. `compile`
   - compile Java source code into class files.
3. `test`
   - run unit tests automatically.
4. `package`
   - package the project into a JAR or WAR, such as `target/*.jar`.
5. `install`
   - install the package into the local Maven repository.
6. `deploy`
   - publish artifact to a remote repository.

For example:

```bash
mvn clean package
```

This command will execute the lifecycle in order, usually including:

```text
clean -> validate -> compile -> test -> package
```

The artifact generated after packaging is the running application package. In this project, the JAR file can be launched directly with:

```bash
java -jar target/spring-boot-user-demo-0.0.1-SNAPSHOT.jar
```

Maven simplifies project build and deployment, and Spring Boot is built on top of this standard lifecycle, making it easy to manage dependencies, compile, test, and package applications.

## 6. Spring MVC request flow

The request flow in Spring MVC is roughly as follows:

```text
Browser / Client
   -> DispatcherServlet
   -> HandlerMapping
   -> Controller
   -> Service
   -> DAO / persistence layer (future MyBatis)
   -> ResponseBody / JSON result
   -> Browser
```

### Flow explanation

1. `DispatcherServlet` receives the HTTP request.
2. `HandlerMapping` matches the request URL to the correct controller method.
3. The controller method is executed, usually annotated with `@RestController`, `@RequestMapping`, `@GetMapping`, or `@PostMapping`.
4. Business logic is processed in the service layer.
5. The returned object is converted into JSON and written back as an HTTP response.

In our project, the request path is:

```text
POST /api/users/register
POST /api/users/login
GET /api/users/{id}
```

The controller code is the entry point, and the service layer encapsulates business logic. This layered design makes the project easy to extend and maintain.

A typical Spring MVC execution chain is:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        // request parsing
        // call service
        // return JSON response
    }
}
```

The `@RequestBody` annotation converts JSON request data into Java object; the returned `Map` or object is serialized by Spring into JSON.

## 7. Cross-domain configuration analysis

Browser-based front ends often run on a different domain or port from the backend API, for example:

- front-end: `http://localhost:3000`
- back-end: `http://localhost:8080`

This causes browser CORS restrictions. To solve this, we configure CORS in the backend.

The core configuration code is:

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

### Key points

- `CorsConfiguration` defines CORS rules.
- `allowedMethods` allows cross-domain HTTP methods.
- `allowedHeaders` allows request headers.
- `allowedOriginPatterns` allows the source domain/port used by the browser.
- `registerCorsConfiguration("/**", config)` applies the rules to all paths.

This ensures a browser front end can call the backend API normally without being blocked by the same-origin policy.

## 8. Why this project is easy to extend

The current version uses an in-memory `Map` to simulate a user database, which is fast and very suitable for learning, demonstration and prototype development. This avoids extra database configuration and keeps the code simple.

When the project later needs MyBatis to achieve durable data storage, the structure can be extended as follows:

- keep `UserController` unchanged
- replace `UserService` internal implementation from in-memory Map to MyBatis mapper
- keep DTO and REST interface stable
- add database config and mapper XML/annotations

This is a classic layered design: controller -> service -> persistence.

## 9. Summary

This project covers the core knowledge points you requested:

- Maven environment and build lifecycle
- Spring Boot entry and project creation
- Spring Web MVC annotation usage
- RESTful API design
- CORS configuration and cross-domain processing
- clear layered architecture suitable for future MyBatis extension

It is light enough to understand quickly, but structured enough to continue expanding.
