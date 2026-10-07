# Module 5: API Testing and Documentation

## Topics Covered
- Postman
- Swagger/OpenAPI documentation

---

## 1. Postman

Postman is a GUI-based API client used to manually and automatically test REST APIs during development.

### Core Usage
1. Create a new **Request** — choose HTTP method (GET/POST/PUT/DELETE) and enter the URL (e.g., `http://localhost:8080/api/employees`).
2. Add **headers** (e.g., `Content-Type: application/json`, `Authorization: Bearer <token>`).
3. Add a **body** (raw JSON) for POST/PUT requests:
   ```json
   {
     "name": "Jane Doe",
     "email": "jane.doe@example.com",
     "age": 30
   }
   ```
4. Click **Send** and inspect the response status, headers, and body.

### Organizing Tests
- **Collections** — group related requests (e.g., "Employee API").
- **Environments** — define variables like `{{baseUrl}}` to switch between `local`, `dev`, `prod` without editing each request.
- **Postman scripts** (`Tests` tab) — assert on responses:
  ```javascript
  pm.test("Status code is 201", function () {
      pm.response.to.have.status(201);
  });

  pm.test("Response has an id", function () {
      const json = pm.response.json();
      pm.expect(json.id).to.exist;
  });
  ```
- **Collection Runner / Newman** — run entire collections headlessly (e.g., in CI pipelines) via the `newman` CLI.

## 2. Swagger / OpenAPI Documentation

OpenAPI (formerly Swagger) is a specification for describing REST APIs (endpoints, request/response schemas, parameters) in a machine-readable format, from which interactive documentation (Swagger UI) can be generated automatically.

Add the dependency:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

Once added, Spring Boot auto-generates:
- OpenAPI JSON spec at `/v3/api-docs`
- Interactive Swagger UI at `/swagger-ui.html`

Enrich the generated documentation with annotations:

```java
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee API", description = "Operations for managing employees")
public class EmployeeController {

    @Operation(summary = "Get employee by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(
            @Parameter(description = "ID of the employee to retrieve")
            @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }
}
```

Global API metadata via a configuration bean:

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeApiInfo() {
        return new OpenAPI().info(new Info()
                .title("Employee Management API")
                .version("1.0")
                .description("REST API for managing employee records"));
    }
}
```

### Postman vs Swagger

| Aspect | Postman | Swagger/OpenAPI |
|---|---|---|
| Primary purpose | Manual/automated API testing | API documentation & contract definition |
| Format | Collections (JSON) | OpenAPI spec (JSON/YAML) |
| Automation | Newman, CI test scripts | Client/server code generation from spec |
| Audience | Developers/QA testing endpoints | Developers & consumers discovering the API |

Both are commonly used together: OpenAPI/Swagger documents the contract, and Postman (which can import an OpenAPI spec directly) is used to exercise and test it.

---

## Key Takeaways
- Postman enables manual and scripted testing of REST endpoints, organized into collections and environments.
- `springdoc-openapi` auto-generates interactive Swagger UI documentation from your Spring MVC controllers.
- Annotations like `@Operation`, `@ApiResponse`, and `@Parameter` enrich generated documentation with meaningful descriptions.
- Postman and Swagger/OpenAPI complement each other for testing and documenting APIs.
