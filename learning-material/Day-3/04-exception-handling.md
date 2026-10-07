# Module 4: Exception Handling

## Topics Covered
- Global exception handling
- `@ControllerAdvice`
- Custom exceptions

---

## 1. Global Exception Handling

Rather than handling exceptions individually in every controller method (repetitive `try/catch` blocks), Spring MVC allows centralizing exception handling so all controllers share consistent error responses.

Without centralized handling:

```java
@GetMapping("/{id}")
public ResponseEntity<Employee> getById(@PathVariable Long id) {
    try {
        return ResponseEntity.ok(employeeService.findById(id));
    } catch (EmployeeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    // repeated in every method...
}
```

With centralized handling, controllers stay clean and simply let exceptions propagate; a single handler translates them into responses.

## 2. `@ControllerAdvice`

`@ControllerAdvice` (or `@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`) defines global exception handlers applied across all `@Controller`/`@RestController` classes.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EmployeeNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

```java
public record ErrorResponse(int status, String message, Instant timestamp) { }
```

**Security note**: never return raw stack traces or internal exception messages (`ex.getMessage()` from unexpected exceptions) to API clients in production — return a generic message and log full details server-side, to avoid leaking implementation details.

## 3. Custom Exceptions

Define domain-specific exceptions to make error handling intention-revealing and to carry contextual data.

```java
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }
}

public class DuplicateEmployeeException extends RuntimeException {
    public DuplicateEmployeeException(String employeeId) {
        super("Employee already exists with id: " + employeeId);
    }
}
```

Throw them from the service layer:

```java
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
```

Optionally annotate the exception itself with `@ResponseStatus` for simple cases where no custom body is needed:

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }
}
```

### Recommended Exception Hierarchy

| Exception | HTTP Status | Use Case |
|---|---|---|
| `EmployeeNotFoundException` | 404 Not Found | Resource lookup failure |
| `DuplicateEmployeeException` | 409 Conflict | Uniqueness constraint violation |
| `MethodArgumentNotValidException` (built-in) | 400 Bad Request | `@Valid` validation failure |
| `AccessDeniedException` (Spring Security) | 403 Forbidden | Authorization failure |
| Generic `Exception` fallback | 500 Internal Server Error | Unexpected/unhandled errors |

---

## Key Takeaways
- Centralize exception-to-response translation using `@RestControllerAdvice` instead of repeating `try/catch` in every controller.
- `@ExceptionHandler` methods map specific exception types to specific HTTP responses.
- Custom exceptions make error scenarios explicit and carry domain context; keep internal details out of client-facing error messages.
