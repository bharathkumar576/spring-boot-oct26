# Module 3: Spring Configuration

## Topics Covered
- XML configuration
- Java-based configuration
- Annotation-based configuration

---

## 1. XML Configuration

The original way of configuring Spring beans, using an XML file that declares beans and their dependencies.

```xml
<!-- applicationContext.xml -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="paymentGateway" class="com.example.StripePaymentGateway" />

    <bean id="orderService" class="com.example.OrderService">
        <constructor-arg ref="paymentGateway" />
    </bean>
</beans>
```

Loaded via:

```java
ApplicationContext context =
    new ClassPathXmlApplicationContext("applicationContext.xml");
```

**Drawbacks**: verbose, no compile-time checking, and refactoring (renaming classes) doesn't automatically update the XML.

## 2. Java-based Configuration

Introduced to replace XML with type-safe, refactorable Java code using `@Configuration` and `@Bean`.

```java
@Configuration
public class AppConfig {

    @Bean
    public PaymentGateway paymentGateway() {
        return new StripePaymentGateway();
    }

    @Bean
    public OrderService orderService(PaymentGateway paymentGateway) {
        return new OrderService(paymentGateway);
    }
}
```

Loaded via:

```java
ApplicationContext context =
    new AnnotationConfigApplicationContext(AppConfig.class);
```

**Benefits**: compile-time safety, IDE support (navigation/refactoring), no reflection-based string lookups.

## 3. Annotation-based Configuration

Uses stereotype annotations directly on classes, combined with classpath scanning, so beans are auto-detected rather than manually declared.

```java
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
}

@Component
public class StripePaymentGateway implements PaymentGateway { }

@Service
public class OrderService {
    private final PaymentGateway paymentGateway;

    @Autowired
    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
```

Spring scans the specified base package(s), finds classes annotated with `@Component` (and its specializations `@Service`, `@Repository`, `@Controller`), and registers them as beans automatically.

### Comparison

| Approach | Configuration Style | Type Safety | Verbosity | Typical Usage Today |
|---|---|---|---|---|
| XML | External `.xml` file | No | High | Legacy projects |
| Java-based (`@Configuration`/`@Bean`) | Java code | Yes | Medium | Explicit bean definitions, third-party beans |
| Annotation-based (`@Component` + scanning) | Annotations on classes | Yes | Low | Default for application-owned classes in Spring Boot |

Modern Spring Boot applications typically combine annotation-based configuration (for application classes) with a small amount of Java-based `@Configuration`/`@Bean` (for third-party or conditional beans). XML is largely legacy.

---

## Key Takeaways
- Three configuration styles exist: XML, Java-based, and annotation-based.
- Java-based and annotation-based configuration are preferred for type safety and maintainability.
- Spring Boot defaults to annotation-driven configuration with component scanning.
