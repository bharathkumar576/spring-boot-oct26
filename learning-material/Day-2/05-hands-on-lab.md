# Module 5: Hands-on Lab

## Topics Covered
- Create Spring Boot project
- Configure profiles
- Build and run applications
- Use Actuator endpoints

---

## Prerequisites
- JDK 21 installed
- Maven (or the Maven wrapper)
- An IDE (VS Code / IntelliJ)
- `curl` or a REST client (Postman/HTTPie) for testing endpoints

## Step 1: Create Spring Boot Project

Generate a project via [Spring Initializr](https://start.spring.io/) with:
- **Build**: Maven
- **Language**: Java 21
- **Dependencies**: `Spring Web`, `Spring Boot Actuator`, `Spring Boot DevTools`

Or via `curl`:

```bash
curl https://start.spring.io/starter.zip \
  -d dependencies=web,actuator,devtools \
  -d javaVersion=21 \
  -d bootVersion=3.4.0 \
  -d groupId=com.example \
  -d artifactId=demo \
  -o demo.zip
```

Resulting project structure:

```
demo/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/example/demo/DemoApplication.java
    │   └── resources/application.properties
    └── test/java/com/example/demo/DemoApplicationTests.java
```

Add a simple REST controller:

```java
// src/main/java/com/example/demo/HelloController.java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }
}
```

## Step 2: Configure Profiles

Create profile-specific property files:

```properties
# src/main/resources/application.properties
spring.application.name=demo
spring.profiles.active=dev
```

```properties
# src/main/resources/application-dev.properties
server.port=8080
logging.level.com.example.demo=DEBUG
```

```properties
# src/main/resources/application-prod.properties
server.port=80
logging.level.com.example.demo=WARN
```

Add a profile-aware bean to verify activation:

```java
@Component
public class ProfileBanner implements CommandLineRunner {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Override
    public void run(String... args) {
        System.out.println("Active profile: " + activeProfile);
    }
}
```

## Step 3: Build and Run Applications

Build the executable JAR:

```bash
mvn clean package
```

Run with the default profile (from `application.properties`):

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Run overriding the active profile:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

Or run directly during development (auto-restart via DevTools on code changes):

```bash
mvn spring-boot:run
```

Verify the REST endpoint:

```bash
curl http://localhost:8080/hello
# Hello from Spring Boot!
```

## Step 4: Use Actuator Endpoints

Expose useful endpoints in `application.properties`:

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
info.app.name=${spring.application.name}
info.app.version=1.0.0
```

Query the endpoints:

```bash
curl http://localhost:8080/actuator/health
# {"status":"UP"}

curl http://localhost:8080/actuator/info
# {"app":{"name":"demo","version":"1.0.0"}}

curl http://localhost:8080/actuator/metrics/jvm.memory.used
```

## Verification Checklist

- [ ] Project builds successfully with `mvn clean package`
- [ ] Application starts and logs the active profile
- [ ] `GET /hello` returns `Hello from Spring Boot!`
- [ ] `/actuator/health` returns `{"status":"UP"}`
- [ ] `/actuator/info` reflects the configured app name/version
- [ ] Switching `--spring.profiles.active` changes the server port/log level as expected

## Stretch Goals
- Add a custom `HealthIndicator` that checks a downstream dependency (e.g., a database connection).
- Enable `/actuator/loggers` and change a log level at runtime without restarting the app.
- Secure Actuator endpoints with Spring Security so only authenticated users can access `/actuator/env`.

---

## Key Takeaways
- Spring Initializr quickly bootstraps a runnable Spring Boot project with the right starters.
- Profiles let the same codebase behave differently across dev/prod without code changes.
- `mvn clean package` + `java -jar` produces a portable, self-contained deployable artifact.
- Actuator endpoints provide immediate operational visibility into a running application.
