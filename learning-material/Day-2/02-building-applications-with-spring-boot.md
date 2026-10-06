# Module 2: Building Applications with Spring Boot

## Topics Covered
- Starter dependencies
- application.properties
- Profiles and environment configuration
- Logging configuration

---

## 1. Starter Dependencies

Starters are convenience dependency descriptors that bundle a set of compatible libraries for a specific purpose, so you don't need to manage versions individually.

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Common starters:

| Starter | Purpose |
|---|---|
| `spring-boot-starter-web` | Build web apps/REST APIs with Spring MVC and embedded Tomcat |
| `spring-boot-starter-data-jpa` | JPA + Hibernate + Spring Data repositories |
| `spring-boot-starter-security` | Authentication and authorization |
| `spring-boot-starter-actuator` | Production-ready monitoring endpoints |
| `spring-boot-starter-test` | JUnit, Mockito, AssertJ, Spring Test |
| `spring-boot-starter-validation` | Bean Validation (Jakarta Validation) |

Version numbers are inherited from the `spring-boot-starter-parent` (or the dependency management BOM), keeping all transitive dependencies mutually compatible.

## 2. `application.properties`

Central place for externalizing configuration instead of hardcoding values.

```properties
# application.properties
server.port=8081
spring.application.name=demo-service

spring.datasource.url=jdbc:postgresql://localhost:5432/demo
spring.datasource.username=postgres
spring.datasource.password=secret

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

Equivalent YAML form (`application.yml`):

```yaml
server:
  port: 8081
spring:
  application:
    name: demo-service
  datasource:
    url: jdbc:postgresql://localhost:5432/demo
    username: postgres
    password: secret
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
```

Values can be injected into beans:

```java
@Component
public class MailProperties {

    @Value("${spring.application.name}")
    private String appName;
}
```

Or bound to a strongly-typed configuration class:

```java
@ConfigurationProperties(prefix = "mail")
public record MailProperties(String host, int port, String from) { }
```

**Security note**: never commit real secrets (passwords, API keys) to `application.properties` in source control — use environment variables, a secrets manager, or `application-local.properties` excluded via `.gitignore`.

## 3. Profiles and Environment Configuration

Profiles let you maintain environment-specific configuration (dev, test, prod) that Spring Boot activates selectively.

```properties
# application.properties
spring.profiles.active=dev
```

```properties
# application-dev.properties
spring.datasource.url=jdbc:h2:mem:devdb
logging.level.com.example=DEBUG
```

```properties
# application-prod.properties
spring.datasource.url=jdbc:postgresql://prod-db:5432/demo
logging.level.com.example=WARN
```

Activate a profile at runtime without changing files:

```bash
java -jar demo.jar --spring.profiles.active=prod
# or
SPRING_PROFILES_ACTIVE=prod java -jar demo.jar
```

Conditionally register beans per profile:

```java
@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return DataSourceBuilder.create().url("jdbc:postgresql://prod-db:5432/demo").build();
    }
}
```

## 4. Logging Configuration

Spring Boot uses Commons Logging internally but defaults to **Logback** for actual output, with sensible console formatting out of the box.

```properties
# application.properties
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.level.com.example.demo=TRACE

logging.file.name=logs/demo.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

Using a logger in code:

```java
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public void placeOrder(Order order) {
        log.info("Placing order {}", order.getId());
    }
}
```

For advanced configuration (rolling file appenders, JSON output), provide a `logback-spring.xml` in `src/main/resources`, which supports Spring profile-aware `<springProfile>` sections.

---

## Key Takeaways
- Starters simplify dependency management by grouping related, version-aligned libraries.
- `application.properties`/`application.yml` externalizes configuration; prefer `@ConfigurationProperties` for structured config.
- Profiles (`application-{profile}.properties` + `@Profile`) enable environment-specific behavior.
- Logging is preconfigured with Logback; log levels and outputs are easily customized per package/profile.
