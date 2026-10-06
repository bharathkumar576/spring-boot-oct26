# Module 4: Build Tools

## Topics Covered
- Maven fundamentals
- Dependency management
- Build lifecycle

---

## 1. Maven Fundamentals

Maven is a build automation and project management tool built around a Project Object Model (POM), declared in `pom.xml`.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
    </parent>

    <properties>
        <java.version>21</java.version>
    </properties>
</project>
```

Key concepts:
- **`groupId`/`artifactId`/`version` (GAV)** — uniquely identifies a project/artifact.
- **`packaging`** — `jar`, `war`, `pom`, etc.
- **`parent` POM** — `spring-boot-starter-parent` provides dependency/plugin version management and sensible defaults (encoding, Java version, resource filtering).
- **Multi-module projects** — a parent `pom.xml` with `packaging=pom` aggregates child modules for larger systems.

## 2. Dependency Management

Maven resolves and downloads dependencies (and their transitive dependencies) from repositories (Maven Central, private/internal repos).

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

- **Version resolution**: with `spring-boot-starter-parent`, versions are inherited from Spring Boot's managed BOM — you typically omit `<version>` for Spring-managed artifacts.
- **Dependency scope**:

| Scope | Description |
|---|---|
| `compile` (default) | Available at compile time and runtime |
| `provided` | Available at compile time, provided by the runtime environment (e.g., servlet API) |
| `runtime` | Not needed for compilation, needed at runtime (e.g., JDBC drivers) |
| `test` | Only available for test compilation/execution |
| `import` | Used in `<dependencyManagement>` to import a BOM |

- **Dependency tree/conflicts**: inspect with `mvn dependency:tree`; resolve version conflicts via explicit `<dependencyManagement>` entries or exclusions.

```bash
mvn dependency:tree
mvn dependency:analyze
```

## 3. Build Lifecycle

Maven defines a standard build lifecycle made up of ordered phases. Running a phase executes all preceding phases in the same lifecycle.

| Phase | Purpose |
|---|---|
| `validate` | Validate project structure/configuration is correct |
| `compile` | Compile main source code |
| `test` | Run unit tests (via Surefire plugin) |
| `package` | Package compiled code into JAR/WAR |
| `verify` | Run integration tests / additional checks |
| `install` | Install the artifact into the local `~/.m2` repository |
| `deploy` | Copy the artifact to a remote repository for sharing |

Common commands:

```bash
mvn clean               # remove target/ directory
mvn compile              # compile sources
mvn test                 # run unit tests
mvn package              # build the JAR/WAR
mvn spring-boot:run      # run the app directly via the Spring Boot Maven plugin
mvn clean install        # full lifecycle through install
```

The **Spring Boot Maven Plugin** repackages the JAR into an executable "fat/uber JAR" containing all dependencies:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## Key Takeaways
- Maven's POM defines project coordinates, dependencies, and plugins; `spring-boot-starter-parent` centralizes version management.
- Dependency scopes control classpath visibility at compile, test, and runtime.
- The build lifecycle (`validate → compile → test → package → verify → install → deploy`) provides a consistent, repeatable build process.
- The Spring Boot Maven plugin repackages your JAR into a runnable, self-contained artifact.
