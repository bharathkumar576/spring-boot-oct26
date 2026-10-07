# Module 2: Building REST APIs

## Topics Covered
- Overview of REST Architecture
- REST principles
- Spring REST
- CRUD operations
- Request parameters
- Path variables
- ResponseEntity

---

## 1. Overview of REST Architecture

REST (Representational State Transfer) is an architectural style for designing networked applications, built around **resources** identified by URLs and manipulated using standard HTTP methods.

- Resources: nouns (e.g., `/employees`, `/orders/{id}`).
- Representations: typically JSON (or XML) describing resource state.
- Stateless communication: each request contains all information needed to process it.

## 2. REST Principles

| Principle | Description |
|---|---|
| **Client-Server** | Separation of concerns between UI (client) and data/logic (server) |
| **Stateless** | No client session state stored on the server between requests |
| **Cacheable** | Responses should define themselves as cacheable or not |
| **Uniform Interface** | Consistent resource identification, manipulation via representations, self-descriptive messages |
| **Layered System** | Client cannot tell whether it's connected directly to the server or an intermediary |
| **Code on Demand** (optional) | Servers can extend client functionality by transferring executable code |

## 3. Spring REST

Spring provides first-class REST support via `@RestController`, `@RequestMapping` family annotations, and automatic JSON serialization (via Jackson).

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
}
```

Jackson automatically converts Java objects to/from JSON as long as `jackson-databind` is on the classpath (included by default in `spring-boot-starter-web`).

## 4. CRUD Operations

Mapping standard CRUD (Create, Read, Update, Delete) operations onto HTTP verbs:

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee saved = employeeService.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.update(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

## 5. Request Parameters

Query string parameters (e.g., `/api/employees?department=IT&page=0`) are bound using `@RequestParam`.

```java
@GetMapping
public List<Employee> search(
        @RequestParam(required = false) String department,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return employeeService.search(department, page, size);
}
```

## 6. Path Variables

URI template variables (e.g., `/api/employees/{id}`) are bound using `@PathVariable`.

```java
@GetMapping("/{id}/department/{deptId}")
public Employee getEmployeeInDepartment(
        @PathVariable Long id,
        @PathVariable Long deptId) {
    return employeeService.findInDepartment(id, deptId);
}
```

| Annotation | Source | Example |
|---|---|---|
| `@PathVariable` | URI path segment | `/employees/{id}` → `id` |
| `@RequestParam` | Query string | `/employees?department=IT` → `department` |
| `@RequestBody` | HTTP request body (JSON) | POST/PUT payload |

## 7. ResponseEntity

`ResponseEntity<T>` gives full control over the HTTP response: status code, headers, and body.

```java
@GetMapping("/{id}")
public ResponseEntity<Employee> getById(@PathVariable Long id) {
    Optional<Employee> employee = employeeService.findById(id);

    return employee
            .map(e -> ResponseEntity.ok()
                    .header("X-Resource-Version", "1")
                    .body(e))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
}
```

Common factory methods: `ResponseEntity.ok()`, `.created(uri)`, `.noContent()`, `.badRequest()`, `.notFound()`, `.status(HttpStatus)`.

---

## Key Takeaways
- REST models data as resources manipulated through standard HTTP verbs, following stateless, uniform-interface principles.
- Spring's `@RestController` + Jackson provide seamless JSON serialization/deserialization.
- `@PathVariable` binds URI segments, `@RequestParam` binds query parameters, and `@RequestBody` binds the request payload.
- `ResponseEntity` allows precise control over status codes, headers, and response bodies.
