# Module 3: Data Validation

## Topics Covered
- Bean Validation
- Custom validations

---

## 1. Bean Validation

Bean Validation (Jakarta Validation, formerly JSR-380) provides a declarative, annotation-based way to validate object state. Spring Boot integrates it automatically via `spring-boot-starter-validation`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Annotate the model with validation constraints:

```java
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 65, message = "Age must be at most 65")
    private int age;

    @Positive(message = "Salary must be positive")
    private BigDecimal salary;

    // getters/setters
}
```

Trigger validation in the controller with `@Valid`:

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody EmployeeRequest request) {
        Employee saved = employeeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```

If validation fails, Spring throws `MethodArgumentNotValidException`, which (combined with a global exception handler) can be translated into a structured `400 Bad Request` response.

### Common Bean Validation Annotations

| Annotation | Description |
|---|---|
| `@NotNull` | Value must not be `null` |
| `@NotBlank` | String must not be `null` or empty/whitespace |
| `@NotEmpty` | Collection/String/array must not be `null` or empty |
| `@Size(min, max)` | String/collection length constraints |
| `@Min` / `@Max` | Numeric lower/upper bounds |
| `@Positive` / `@Negative` | Numeric sign constraints |
| `@Email` | Must be a valid email format |
| `@Pattern(regexp)` | Must match a regular expression |
| `@Past` / `@Future` | Date must be in the past/future |

## 2. Custom Validations

When built-in constraints aren't sufficient, define a custom constraint annotation and validator.

```java
// 1. Define the annotation
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmployeeIdValidator.class)
public @interface UniqueEmployeeId {
    String message() default "Employee ID already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// 2. Implement the validator
@Component
public class UniqueEmployeeIdValidator implements ConstraintValidator<UniqueEmployeeId, String> {

    private final EmployeeRepository employeeRepository;

    public UniqueEmployeeIdValidator(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean isValid(String employeeId, ConstraintValidatorContext context) {
        return employeeId != null && !employeeRepository.existsByEmployeeId(employeeId);
    }
}

// 3. Apply it to a field
public class EmployeeRequest {

    @UniqueEmployeeId
    private String employeeId;
}
```

Custom validators can be registered as Spring beans, so they can inject repositories/services — enabling database-aware validation rules (e.g., uniqueness checks) that plain annotations cannot express.

### Class-level (cross-field) validation

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {
    String message() default "Passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
```

---

## Key Takeaways
- Bean Validation annotations (`@NotNull`, `@Size`, `@Email`, etc.) declaratively enforce field-level constraints.
- `@Valid` on a `@RequestBody` parameter triggers validation automatically in Spring MVC controllers.
- Custom constraint annotations + `ConstraintValidator` implementations handle validation logic beyond the built-in annotations, including database-aware checks.
- Combine validation with a global exception handler to return consistent, structured error responses.
