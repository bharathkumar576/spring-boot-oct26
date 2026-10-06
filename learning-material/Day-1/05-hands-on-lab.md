# Module 5: Hands-on Lab

## Topics Covered
- Create first Spring application
- Configure beans using annotations
- Implement Dependency Injection

---

## Prerequisites
- JDK 21 installed
- Maven (or the Maven wrapper)
- An IDE (VS Code / IntelliJ)

## Step 1: Create First Spring Application

Generate a project via [Spring Initializr](https://start.spring.io/) or manually with Maven, using dependency `spring-context`.

`pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.2.0</version>
    </dependency>
</dependencies>
```

Project structure:

```
src/main/java/com/example/
├── App.java
├── config/AppConfig.java
├── service/OrderService.java
└── gateway/PaymentGateway.java
└── gateway/StripePaymentGateway.java
```

## Step 2: Configure Beans Using Annotations

Define the interface and its implementation as Spring-managed components:

```java
// gateway/PaymentGateway.java
public interface PaymentGateway {
    void processPayment(double amount);
}

// gateway/StripePaymentGateway.java
@Component
public class StripePaymentGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " via Stripe");
    }
}
```

Enable component scanning through a configuration class:

```java
// config/AppConfig.java
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
}
```

## Step 3: Implement Dependency Injection

Create a service that depends on `PaymentGateway`, injected via the constructor:

```java
// service/OrderService.java
@Service
public class OrderService {

    private final PaymentGateway paymentGateway;

    @Autowired
    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void placeOrder(double amount) {
        System.out.println("Placing order...");
        paymentGateway.processPayment(amount);
    }
}
```

Wire everything together and run the application:

```java
// App.java
public class App {
    public static void main(String[] args) {
        ApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

        OrderService orderService = context.getBean(OrderService.class);
        orderService.placeOrder(150.00);
    }
}
```

### Expected Output

```
Placing order...
Processing payment of $150.0 via Stripe
```

## Verification Checklist

- [ ] Project builds successfully with Maven (`mvn compile`)
- [ ] `AnnotationConfigApplicationContext` starts without errors
- [ ] `OrderService` bean is retrieved from the context
- [ ] `PaymentGateway` implementation is injected via constructor (not `new`-ed manually)
- [ ] Console output confirms the order and payment flow

## Stretch Goals
- Add a second `PaymentGateway` implementation (e.g., `PaypalPaymentGateway`) and resolve the ambiguity using `@Qualifier`.
- Externalize the payment provider name using `@Value("${payment.provider}")` and a `application.properties` file.
- Add `@PostConstruct`/`@PreDestroy` logging to observe the bean lifecycle.

---

## Key Takeaways
- A minimal Spring application only needs `spring-context` and a configuration class.
- `@ComponentScan` + stereotype annotations remove the need for manual bean wiring.
- Constructor injection keeps dependencies explicit and testable.
