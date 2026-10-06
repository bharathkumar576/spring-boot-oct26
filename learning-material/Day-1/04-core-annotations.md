# Module 4: Core Annotations

## Topics Covered
- `@Component`
- `@Service`
- `@Repository`
- `@Controller`
- `@Autowired`
- `@Qualifier`
- `@Value`

---

## 1. `@Component`

The generic stereotype annotation that marks a class as a Spring-managed bean, eligible for component scanning.

```java
@Component
public class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}
```

## 2. `@Service`

A specialization of `@Component` used to mark classes that hold **business logic**. Functionally identical to `@Component`, but improves readability and intent.

```java
@Service
public class OrderService {
    public void placeOrder(Order order) {
        // business logic
    }
}
```

## 3. `@Repository`

A specialization of `@Component` for the **persistence layer** (DAOs). It also enables automatic translation of persistence-specific exceptions (e.g., JDBC/JPA exceptions) into Spring's unchecked `DataAccessException` hierarchy.

```java
@Repository
public class EmployeeRepository {
    public Employee findById(Long id) {
        // data access logic
        return null;
    }
}
```

## 4. `@Controller`

A specialization of `@Component` for the **web/presentation layer** in Spring MVC. Combined with `@RequestMapping`/`@GetMapping`/etc., it handles incoming HTTP requests. `@RestController` = `@Controller` + `@ResponseBody`.

```java
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeService.findById(id);
    }
}
```

## 5. `@Autowired`

Marks a constructor, setter, or field for automatic dependency injection by type. Spring resolves the dependency from the container.

```java
@Component
public class OrderService {
    private final PaymentGateway paymentGateway;

    @Autowired // optional on a single constructor since Spring 4.3+
    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
```

If no matching bean is found, Spring throws `NoSuchBeanDefinitionException`. If multiple beans of the same type exist, Spring throws `NoUniqueBeanDefinitionException` unless disambiguated (see `@Qualifier`).

## 6. `@Qualifier`

Used alongside `@Autowired` to resolve ambiguity when multiple beans of the same type are available, by specifying the bean name.

```java
public interface PaymentGateway { }

@Component("stripeGateway")
public class StripePaymentGateway implements PaymentGateway { }

@Component("paypalGateway")
public class PaypalPaymentGateway implements PaymentGateway { }

@Service
public class OrderService {
    private final PaymentGateway paymentGateway;

    @Autowired
    public OrderService(@Qualifier("stripeGateway") PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
```

## 7. `@Value`

Injects values from properties files, environment variables, or SpEL (Spring Expression Language) expressions into fields, constructor parameters, or method parameters.

```java
@Component
public class MailConfig {

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.port:25}") // default value 25 if property missing
    private int mailPort;

    @Value("#{systemProperties['user.region']}")
    private String userRegion;
}
```

### Summary Table

| Annotation | Layer / Purpose |
|---|---|
| `@Component` | Generic bean |
| `@Service` | Business/service layer |
| `@Repository` | Persistence/DAO layer (adds exception translation) |
| `@Controller` | Web/presentation layer |
| `@Autowired` | Automatic dependency injection by type |
| `@Qualifier` | Disambiguate between multiple candidate beans |
| `@Value` | Inject property/environment/SpEL values |

---

## Key Takeaways
- `@Service`, `@Repository`, and `@Controller` are semantic specializations of `@Component`.
- `@Autowired` performs by-type injection; use `@Qualifier` when multiple candidates exist.
- `@Value` bridges external configuration (properties files, env vars) into beans.
