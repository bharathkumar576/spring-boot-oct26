# Module 2: Core Spring Concepts

## Topics Covered
- IoC (Inversion of Control)
- Dependency Injection (DI)
- Bean lifecycle
- Bean scopes
- ApplicationContext vs BeanFactory

---

## 1. IoC (Inversion of Control)

Inversion of Control is a design principle where the control of object creation and lifecycle management is transferred from the application code to a container/framework.

- Traditionally: your code creates and manages its own dependencies (`new` keyword).
- With IoC: the **Spring container** creates objects (beans), configures them, and manages their lifecycle.
- Benefits: loose coupling, easier testing (mocking), centralized configuration.

## 2. Dependency Injection (DI)

DI is the mechanism through which IoC is implemented in Spring. Instead of a class creating its dependencies, they are "injected" by the container.

### Types of DI

```java
// Constructor Injection (recommended)
@Component
public class OrderService {
    private final PaymentGateway paymentGateway;

    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}

// Setter Injection
@Component
public class OrderService {
    private PaymentGateway paymentGateway;

    @Autowired
    public void setPaymentGateway(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}

// Field Injection (discouraged for testability reasons)
@Component
public class OrderService {
    @Autowired
    private PaymentGateway paymentGateway;
}
```

**Best practice**: Prefer constructor injection — it makes dependencies explicit, supports immutability (`final` fields), and works well with unit testing.

## 3. Bean Lifecycle

A Spring bean goes through a well-defined lifecycle managed by the container:

1. **Instantiation** — container creates the bean instance.
2. **Populate properties** — dependencies are injected.
3. **`BeanNameAware` / `BeanFactoryAware` / `ApplicationContextAware`** callbacks (if implemented).
4. **`@PostConstruct`** / `InitializingBean.afterPropertiesSet()` / custom `init-method` — post-initialization hook.
5. **Bean is ready for use.**
6. **`@PreDestroy`** / `DisposableBean.destroy()` / custom `destroy-method` — cleanup hook before container shutdown.

```java
@Component
public class ConnectionPool {

    @PostConstruct
    public void init() {
        // open connections
    }

    @PreDestroy
    public void cleanup() {
        // close connections
    }
}
```

## 4. Bean Scopes

| Scope | Description |
|---|---|
| `singleton` (default) | One shared instance per Spring container |
| `prototype` | New instance created every time the bean is requested |
| `request` | One instance per HTTP request (web-aware contexts) |
| `session` | One instance per HTTP session (web-aware contexts) |
| `application` | One instance per `ServletContext` |
| `websocket` | One instance per WebSocket session |

```java
@Component
@Scope("prototype")
public class ReportGenerator {
    // a new instance is created each time it's injected/looked up
}
```

## 5. ApplicationContext vs BeanFactory

| Aspect | `BeanFactory` | `ApplicationContext` |
|---|---|---|
| Loading | Lazy — beans created on first request | Eager by default — singletons instantiated at startup |
| Features | Basic DI container | Adds AOP, event propagation, internationalization (i18n), annotation-config support |
| Use case | Lightweight, resource-constrained environments | Standard choice for almost all Spring applications |

In practice, `ApplicationContext` (e.g., `AnnotationConfigApplicationContext`, `ClassPathXmlApplicationContext`) is used almost universally; `BeanFactory` is rarely used directly today.

---

## Key Takeaways
- IoC inverts control of object creation to the Spring container; DI is the implementation mechanism.
- Prefer constructor injection for mandatory dependencies.
- Understand the bean lifecycle hooks (`@PostConstruct`/`@PreDestroy`) for resource management.
- Choose the correct bean scope based on statefulness requirements.
- `ApplicationContext` is the go-to container interface in modern Spring applications.
