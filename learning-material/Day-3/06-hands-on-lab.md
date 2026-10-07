# Module 6: Hands-on Lab

## Topics Covered
- Develop Employee Management REST API
- Implement CRUD operations
- Add validation and exception handling
- Test APIs using Postman

---

## Prerequisites
- JDK 21 installed
- Maven (or the Maven wrapper)
- An IDE (VS Code / IntelliJ)
- Postman installed

## Step 1: Develop Employee Management REST API

Generate a project via [Spring Initializr](https://start.spring.io/) with dependencies: `Spring Web`, `Validation`, `Spring Boot DevTools`.

Project structure:

```
src/main/java/com/example/demo/
├── DemoApplication.java
├── model/Employee.java
├── dto/EmployeeRequest.java
├── exception/EmployeeNotFoundException.java
├── exception/GlobalExceptionHandler.java
├── repository/EmployeeRepository.java
├── service/EmployeeService.java
└── controller/EmployeeController.java
```

Define the model and an in-memory repository (no database needed for this lab):

```java
// model/Employee.java
public class Employee {
    private Long id;
    private String name;
    private String email;
    private int age;

    // constructors, getters, setters
}
```

```java
// repository/EmployeeRepository.java
@Repository
public class EmployeeRepository {
    private final Map<Long, Employee> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idGenerator.getAndIncrement());
        }
        store.put(employee.getId(), employee);
        return employee;
    }

    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Employee> findAll() {
        return new ArrayList<>(store.values());
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}
```

## Step 2: Implement CRUD Operations

```java
// dto/EmployeeRequest.java
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    private int age;

    // getters/setters
}
```

```java
// service/EmployeeService.java
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee create(EmployeeRequest request) {
        Employee employee = new Employee(null, request.getName(), request.getEmail(), request.getAge());
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee update(Long id, EmployeeRequest request) {
        Employee existing = findById(id);
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setAge(request.getAge());
        return employeeRepository.save(existing);
    }

    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }
}
```

```java
// controller/EmployeeController.java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(request));
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        return employeeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Step 3: Add Validation and Exception Handling

```java
// exception/EmployeeNotFoundException.java
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }
}
```

```java
// exception/GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EmployeeNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "message", ex.getMessage(),
                "timestamp", Instant.now().toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
```

## Step 4: Test APIs Using Postman

Start the application:

```bash
mvn spring-boot:run
```

Create a Postman collection "Employee Management API" with the following requests:

| Request | Method | URL | Body |
|---|---|---|---|
| Create Employee | POST | `http://localhost:8080/api/employees` | `{"name":"Jane Doe","email":"jane@example.com","age":30}` |
| Get All Employees | GET | `http://localhost:8080/api/employees` | — |
| Get Employee by ID | GET | `http://localhost:8080/api/employees/1` | — |
| Update Employee | PUT | `http://localhost:8080/api/employees/1` | `{"name":"Jane Smith","email":"jane.smith@example.com","age":31}` |
| Delete Employee | DELETE | `http://localhost:8080/api/employees/1` | — |

Add test assertions in the **Tests** tab of the "Create Employee" request:

```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response contains employee id", function () {
    pm.expect(pm.response.json().id).to.exist;
});
```

Verify negative/error scenarios:
- `POST` with an invalid email → expect `400 Bad Request` with field error details.
- `GET /api/employees/999` (non-existent ID) → expect `404 Not Found`.

## Verification Checklist

- [ ] All five CRUD endpoints respond correctly
- [ ] Invalid input (`@Valid`) returns `400` with field-level error messages
- [ ] Requesting a non-existent employee ID returns `404` with a structured error body
- [ ] Postman collection covers create, read, update, delete, and error scenarios
- [ ] Application runs via `mvn spring-boot:run` without errors

## Stretch Goals
- Add `springdoc-openapi` and verify the same endpoints appear correctly in Swagger UI.
- Export the Postman collection and run it headlessly with `newman`.
- Replace the in-memory repository with Spring Data JPA (preview of Day 4).

---

## Key Takeaways
- A complete REST API combines controller, service, and repository layers with clear responsibilities.
- `@Valid` + a global `@RestControllerAdvice` handler produce consistent validation and error responses.
- Postman collections (with scripted assertions) provide a repeatable way to manually and automatically verify API behavior.
