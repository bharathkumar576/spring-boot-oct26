# Module 3: Spring Boot Features

## Topics Covered
- Embedded Tomcat
- Actuator
- DevTools

---

## 1. Embedded Tomcat

Spring Boot applications are packaged as executable JARs with an embedded servlet container (Tomcat by default), so there's no need to install or configure an external application server.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- Includes spring-boot-starter-tomcat transitively -->
</dependency>
```

Run directly:

```bash
mvn spring-boot:run
# or
java -jar demo-0.0.1-SNAPSHOT.jar
```

Customize the embedded server via properties:

```properties
server.port=8443
server.servlet.context-path=/api
server.tomcat.max-threads=200
server.tomcat.connection-timeout=5s
```

Switch to a different embedded container by excluding Tomcat and adding Jetty/Undertow:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

## 2. Actuator

`spring-boot-starter-actuator` adds production-ready features for monitoring and managing an application via HTTP endpoints (or JMX).

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Enable and expose endpoints:

```properties
management.endpoints.web.exposure.include=health,info,metrics,env
management.endpoint.health.show-details=always
```

Common endpoints:

| Endpoint | Purpose |
|---|---|
| `/actuator/health` | Application health/liveness status |
| `/actuator/info` | Custom build/application info |
| `/actuator/metrics` | JVM, HTTP, and custom metrics |
| `/actuator/env` | Environment properties |
| `/actuator/loggers` | View/change log levels at runtime |
| `/actuator/beans` | List all Spring beans in the context |

**Security note**: restrict sensitive Actuator endpoints (`env`, `beans`, `heapdump`) behind authentication in production — never expose them publicly without access control, since they can leak configuration and internals.

```java
@Component
public class VersionInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("version", "1.0.0");
    }
}
```

## 3. DevTools

`spring-boot-devtools` improves the development-time feedback loop with automatic restarts, live reload, and sensible dev-only defaults.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

Key features:
- **Automatic restart** — the app restarts automatically when classpath files change (uses two classloaders to keep restarts fast).
- **LiveReload** — triggers a browser refresh automatically via the LiveReload protocol (browser extension required).
- **Property defaults for development** — disables template caching, enables H2 console, etc.
- **Remote debugging support** for remote applications (used carefully, and never in production).

**Important**: DevTools is automatically disabled in a fully packaged production JAR (`optional=true` / excluded via repackage), so it has no effect once deployed — but should never intentionally be included in production dependency scopes.

---

## Key Takeaways
- Spring Boot embeds Tomcat (or Jetty/Undertow) so applications are self-contained runnable JARs.
- Actuator exposes operational endpoints for health, metrics, and diagnostics — secure sensitive endpoints in production.
- DevTools speeds up the local development loop via automatic restarts and LiveReload, and is excluded from production builds.
