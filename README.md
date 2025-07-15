# Spring Python Executor
[![Maven Central](https://img.shields.io/maven-central/v/io.github.w4t3rcs/spring-boot-python-executor.svg)](https://central.sonatype.com/artifact/io.github.w4t3rcs/spring-boot-python-executor)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

## Overview

**Spring Python Executor** is a powerful Spring Boot library that enables seamless integration of Python scripts with Java applications. It bridges the gap between Java and Python ecosystems, allowing you to leverage the strengths of both languages in a single application.

### Why Spring Python Executor?

- **Combine the best of both worlds**: Use Java's enterprise capabilities with Python's data science and scripting strengths
- **Simplified integration**: No need for complex inter-process communication or REST APIs
- **AOP-based approach**: Execute Python code before or after Java method calls with minimal boilerplate
- **Flexible execution options**: Run Python code directly, from files, or with dynamic SpEL expressions
- **Enterprise-ready**: Designed for Spring Boot applications with configuration-driven setup

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Configuration](#configuration)
  - [Basic Configuration](#basic-configuration)
  - [Py4J Configuration](#py4j-configuration)
  - [Spelython Configuration](#spelython-configuration)
- [Usage](#usage)
  - [Basic Python Execution](#basic-python-execution)
  - [Spelython (SpEL + Python)](#spelython-spel--python)
  - [Py4J Integration](#py4j-integration)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Links](#links)

## Features

- **Aspect-Oriented Programming (AOP)**
  - Execute Python scripts before or after Java method calls using simple annotations
  - Minimal code changes required to integrate Python functionality
  - Clean separation of concerns between Java business logic and Python processing

- **Multiple Execution Methods**
  - **Direct Script Execution**: Run Python code directly from strings embedded in Java
  - **File Execution**: Execute Python scripts from files in your classpath for better organization
  - **SpEL Integration**: Use Spring Expression Language within your Python scripts for dynamic content

- **Py4J Integration**
  - Seamlessly interact with Python code from Java and vice versa
  - Share objects between Java and Python runtimes
  - Call Java methods from Python and Python functions from Java

- **Enterprise Features**
  - **ProcessBuilder Integration**: Efficiently handle Python processes in a Spring Boot environment
  - **Configuration Driven**: Flexible configuration using Spring Boot properties and annotations
  - **Customizable**: Extend the library with your own aspects and handlers to fit your specific needs
  - **Logging Support**: Comprehensive logging of Python execution for debugging and monitoring

## Installation

### Prerequisites

Before you begin, ensure you have:

- **Java 17** or higher
- **Maven** or **Gradle** (for dependency management)
- **Python 3.x** installed and available in your PATH
- **Spring Boot 3.x** application

> **Note**: Py4J is automatically included as a dependency when you add Spring Python Executor to your project.

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.w4t3rcs</groupId>
    <artifactId>spring-boot-python-executor</artifactId>
    <version>1.2.0</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```groovy
implementation 'io.github.w4t3rcs:spring-boot-python-executor:1.2.0'
```

### Verifying Installation

After adding the dependency, Spring Python Executor will be automatically configured with sensible defaults.
You can verify the installation by adding a simple Python execution to your application:

```java
@Service
public class TestService {
    @PythonBefore("print('Spring Python Executor is working!')")
    public void testMethod() {
        // This will execute the Python code before this method runs
    }
}
```

## Configuration

Spring Python Executor uses Spring Boot's property-based configuration system. All configuration properties are prefixed with `spring.python`.

### Basic Configuration

These core settings control how Python is executed in your application:

```properties
# Python configuration
spring.python.start-command=python
spring.python.path=/python/
spring.python.loggable=true
```

| Property        | Description                              | Default    |
|-----------------|------------------------------------------|------------|
| `start-command` | Command used to start Python interpreter | `python`   |
| `path`          | Classpath location for Python scripts    | `/python/` |
| `loggable`      | Whether to log Python output             | `true`     |

### Py4J Configuration

Py4J enables bidirectional calls between Java and Python. Configure it with these properties:

```properties
# Py4J configuration
spring.python.py4j.enabled=true
spring.python.py4j.import-line=from py4j.java_gateway import JavaGateway\ngateway = JavaGateway()
spring.python.py4j.host=127.0.0.1
spring.python.py4j.port=25333
spring.python.py4j.python-port=25334
spring.python.py4j.connect-timeout=0
spring.python.py4j.read-timeout=0
spring.python.py4j.loggable=true
```

| Property          | Description                         | Default                                                              |
|-------------------|-------------------------------------|----------------------------------------------------------------------|
| `enabled`         | Enable Py4J integration             | `false`                                                              |
| `import-line`     | Automatically import Python modules | `from py4j.java_gateway import JavaGateway\ngateway = JavaGateway()` |
| `host`            | Host address for Py4J gateway       | `127.0.0.1`                                                          |
| `port`            | Port for Java gateway               | `25333`                                                              |
| `python-port`     | Port for Python gateway             | `25334`                                                              |
| `connect-timeout` | Connection timeout in milliseconds  | `0` (no timeout)                                                     |
| `read-timeout`    | Read timeout in milliseconds        | `0` (no timeout)                                                     |
| `loggable`        | Whether to log Py4J logs            | `true`                                                               |
### Spelython Configuration

Spelython combines Spring Expression Language (SpEL) with Python, allowing dynamic expression evaluation:

```properties
# Spelython configuration
spring.python.spelython.regex=spel\\{.+?}
spring.python.spelython.spel-local-variable-index=#
spring.python.spelython.spel-position-from-start=5
spring.python.spelython.spel-position-from-end=1
```

| Property                    | Description                                             | Default       |
|-----------------------------|---------------------------------------------------------|---------------|
| `regex`                     | Regular expression pattern to identify SpEL expressions | `spel\\{.+?}` |
| `spel-local-variable-index` | Prefix for local variable references                    | `#`           |
| `spel-position-from-start`  | Characters to skip from start of matched pattern        | `5`           |
| `spel-position-from-end`    | Characters to skip from end of matched pattern          | `1`           |

> **Example**: With default settings, the expression `spel{@service.getValue()}` would extract `@service.getValue()` for evaluation.

### Script Result Configuration

Spring Boot Python Executor allows retrieving an object from a Python script as a JSON parsed object

```properties
# Spelython configuration
spring.python.result.regex=o4java$\\{.+?}
spring.python.result.appearance=r4java$
spring.python.result.position-from-start=8
spring.python.result.position-from-end=1
```

| Property              | Description                                                                   | Default          |
|-----------------------|-------------------------------------------------------------------------------|------------------|
| `regex`               | Regular expression pattern to identify script result expression               | `o4java$\\{.+?}` |
| `appearance`          | Special identifier for result object in the output of Python script execution | `r4java$`        |
| `position-from-start` | Characters to skip from start of matched pattern                              | `5`              |
| `position-from-end`   | Characters to skip from end of matched pattern                                | `1`              |

> **Example**: With default settings, the expression `o4java${myPythonVariable}` would extract `myPythonVariable` as the result of the Python script.

## Usage

Spring Python Executor offers multiple ways to integrate Python code into your Java application. 
This section covers the three main approaches: Basic Python Execution, Spelython, and Py4J Integration.

### Basic Python Execution

The simplest way to execute Python code from your Java application.

#### Using Annotations

Spring Python Executor provides two primary annotations for executing Python code:

##### @PythonBefore

Executes Python code before a method call:

**Basic Example:**
```java
@PythonBefore("print('Hello from Python before Java method')")
public void doSomething() {
    // Method body executes after Python code
    System.out.println("Java method executing");
}
```

##### @PythonAfter

Executes Python code after a method call:

**Basic Example:**
```java
@PythonAfter("print('Hello from Python after Java method')")
public void doSomething() {
    // Method body executes before Python code
    System.out.println("Java method executing");
}
```

##### Executing Python Script Files

Instead of inline code, you can execute Python scripts stored in files:

**Basic Example:**
```java
// File should be in the configured Python path (default: /python/)
@PythonBefore("example.py")
public void doSomething() {
    // Python script runs before this method
    System.out.println("Java method executing");
}
```

#### Using PythonExecutor Service

For more programmatic control, inject and use the `PythonExecutor` service:

**Basic Example:**
```java
@Service
public class ExampleService {
    private final PythonExecutor pythonExecutor;

    @Autowired
    public ExampleService(PythonExecutor pythonExecutor) {
        this.pythonExecutor = pythonExecutor;
    }

    public void doSomething(String data) {
        // Create a simple Python script
        String script = "print('Hello world!')";

        // Execute the script
        pythonExecutor.execute(script, null);
    }
}
```

**Advanced Example:**

```java
@Service
public class ExampleService {
  private final PythonExecutor pythonExecutor;
  private final PythonResolver resultResolver;

  @Autowired
  public ExampleService(PythonExecutor pythonExecutor,
                        @Qualifier("resultResolver") PythonResolver resultResolver) {
    this.pythonExecutor = pythonExecutor;
    this.resultResolver = resultResolver;
  }

  public void doSomething(String data) {
    // Create a simple Python script
    String script = "myPythonVar = 'Hello world!'\n" +
            "print(myPythonVar)\n";
            "o4java{myPythonVar}";

    // Execute the script
    PythonUtil.executeScript(script, String.class, pythonExecutor, null, resultResolver);
  }
}
```

### Spelython (SpEL + Python)

Spelython combines Spring Expression Language (SpEL) with Python, allowing dynamic content in your Python code.

#### How Spelython Works

- Python scripts automatically include `import json` at the beginning
- SpEL expressions are enclosed in `spel{...}` syntax
- Expressions are evaluated in the Spring context and replaced with their values
- Complex objects are converted to JSON and loaded as Python objects

#### Using Annotations

##### @SpelythonBefore

**Basic Example:**
```java
@SpelythonBefore(
    "user = spel{#user}\n" +
    "print(f'Processing user: {spel{#userId}} {user[\"name\"]}, age: {user[\"age\"]}')"
)
public void doSomething(User user, @SpelParam("userId") String id) {
    // Method implementation
}
```

##### @SpelythonAfter

**Basic Example:**
```java
@SpelythonAfter(
        "user = spel{#user}\n" + 
        "print(f'Processing user: spel{#userId} {user[\"name\"]}, age: {user[\"age\"]}')\n" +
        "print(spel{#result})"
)
public Result doSomething(User user, @SpelParam("userId") String id) {
    // Method implementation
    return Result();
}
```

##### With Script Files

**Basic Example:**
```java
@SpelythonBefore("example.py")  // File contains SpEL expressions
public void doSomething() {
    // Method implementation
}
```

#### Using SpelythonResolver Programmatically

**Basic Example:**
```java
@Service
public class SimpleProcessingService {
    private final PythonExecutor pythonExecutor;
    private final SpelythonResolver spelythonResolver;

    @Autowired
    public SimpleProcessingService(
            PythonExecutor pythonExecutor,
            SpelythonResolver spelythonResolver) {
        this.pythonExecutor = pythonExecutor;
        this.spelythonResolver = spelythonResolver;
    }

    public void processSimpleData(String username, int age) {
        // Create script with SpEL expressions
        String script = 
            "username = spel{#username}\n" +
            "age = spel{#age}\n" +
            "print(f'Processing data for {username}, age: {age}')\n" +
            "\n" +
            "# Use a Spring bean\n" +
            "current_time = spel{@timeService.getCurrentTime()}\n" +
            "print(f'Current time: {current_time}')";

        // Create parameters map
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("age", age);
        PythonUtil.executeScript(script, null, pythonExecutor, params, spelythonResolver);
    }
}
```

### Py4J Integration

Py4J enables bidirectional communication between Java and Python, allowing each language to access objects in the other.

#### Enabling Py4J

Add the `@EnablePy4J` annotation to your main class:

```java
@EnablePy4J
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Or enable it in your properties:

```properties
spring.python.py4j.enabled=true
```

#### Using Annotations

##### @Py4JBefore

**Basic Example:**
```java
@Py4JBefore(
    "java_service = gateway.entry_point.getService()\n" +
    "result = java_service.calculateValue(42)\n" +
    "print(f'Java service returned: {result}')"
)
public void doSomething() {
    // Method implementation
}
```

##### @Py4JAfter

**Basic Example:**
```java
@Py4JAfter(
    "result_obj = gateway.jvm.java.util.HashMap()\n" +
    "result_obj.put('status', 'completed')\n" +
    "result_obj.put('timestamp', gateway.jvm.java.time.Instant.now().toString())\n" +
    "print(f'Created result object: {result_obj}')"
)
public void doSomething() {
    // Method implementation
}
```

##### With Script Files

**Basic Example:**
```java
@Py4JBefore("example.py")
public void doSomething() {
    // Method implementation
}
```

#### Using Py4JResolver Programmatically

**Basic Example:**
```java
@Service
public class SimpleIntegrationService {
    private final PythonExecutor pythonExecutor;
    private final Py4JResolver py4JResolver;

    @Autowired
    public SimpleIntegrationService(
            PythonExecutor pythonExecutor,
            Py4JResolver py4JResolver) {
        this.pythonExecutor = pythonExecutor;
        this.py4JResolver = py4JResolver;
    }

    public void performSimpleOperation() {
        // Create a script that uses the Py4J gateway
        String script =
            "java_list = gateway.jvm.java.util.ArrayList()\n" +
            "java_list.add('Item 1')\n" +
            "java_list.add('Item 2')\n" +
            "\n" +
            "# Call Java methods\n" +
            "service = gateway.entry_point.getDataService()\n" +
            "result = service.processItems(java_list)\n" +
            "print(f'Java processing result: {result}')";
        PythonUtil.executeScript(script, null, pythonExecutor, null, py4JResolver);
    }
}
```

## Architecture

Spring Python Executor is built on a modular architecture that enables seamless integration between Java and Python. The library follows Spring's best practices for extensibility and configuration.

### Core Components

#### Execution Layer

- **PythonExecutor**: The central service responsible for executing Python code
  - Manages Python process lifecycle
  - Configurable through properties

- **PythonFileHandler**: Manages Python script files
  - Locates scripts in the classpath
  - Reads and processes script content
  - Supports dynamic script modification

- **ProcessStarter**: Starts process using command
  - Creates a new process
  - Handles both file or line scripts

- **ProcessHandler**: Manages a script process
  - Handles input/output streams

- **ProcessFinisher**: Finalize a script process
  - Logs script exit
  - Destroys a script process object

#### Integration Layer

- **AOP Aspects**: Intercept method calls to execute Python code
  - **PythonCommandAspect**: Handles basic Python execution annotations
  - **SpelythonAspect**: Processes SpEL-integrated Python annotations
  - **Py4JAspect**: Manages Py4J-integrated Python annotations

- **Resolvers**: Transform Python code before execution
  - **SpelythonResolver**: Evaluates and replaces SpEL expressions in Python code
  - **Py4JResolver**: Prepares Python code for Py4J execution
  - **ResultResolver**: Resolves Python script result for use in Java code

#### Configuration Layer

- **Properties**: Configures the library through Spring Boot properties
  - **PythonProperties**: Core Python execution settings
  - **Py4JProperties**: Py4J integration settings
  - **SpelythonProperties**: SpEL integration settings
  - **ResultProperties**: Python code result integration settings

- **Conditions**: Controls conditional bean creation
  - **Py4JCondition**: Determines when to enable Py4J integration

### Data Flow

1. **Interception**: AOP aspects intercept annotated method calls
2. **Resolution**: Resolvers process the Python code, evaluating expressions and preparing parameters
3. **Execution**: PythonExecutor runs the processed Python code
4. **Integration**: Results are optionally passed back to Java

## Contributing

We welcome contributions from the community!
Whether you're fixing bugs, improving documentation, or adding new features, your help is appreciated.

### Contribution Guidelines

- **Code Style**: Follow the existing code style and formatting
- **Documentation**: Update documentation for any changed functionality
- **Tests**: Add tests for new features and ensure all tests pass
- **Commit Messages**: Write clear, concise commit messages
- **Pull Requests**: Keep PRs focused on a single topic

### Reporting Issues

Found a bug or have a feature request?
Please [open an issue](https://github.com/w4t3rcs/spring-python-executor/issues/new) with:

- A clear, descriptive title
- A detailed description of the issue or feature request
- Steps to reproduce (for bugs)
- Expected and actual behavior (for bugs)
- Any relevant logs or screenshots

## License

Spring Python Executor is licensed under the Apache License 2.0.

For the full license text, see the [LICENSE](https://github.com/w4t3rcs/spring-python-executor/blob/master/LICENSE.txt) file.

## Contact

If you need help with Spring Python Executor or have questions:

- **GitHub Issues**: For bug reports and feature requests
- **GitHub Discussions**: For general questions and discussions
- **Email**: [w4t3rofficial@gmail.com](mailto:w4t3rofficial@gmail.com)

## Resources

### Links

- [GitHub Repository](https://github.com/w4t3rcs/spring-python-executor)
- [Maven Central](https://central.sonatype.com/artifact/io.github.w4t3rcs/spring-boot-python-executor)
- [Issue Tracker](https://github.com/w4t3rcs/spring-python-executor/issues)

### Related Documentation

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring AOP Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop)
- [Spring Expression Language (SpEL)](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions)
- [Py4J Documentation](https://www.py4j.org/getting_started.html)
- [Python Documentation](https://docs.python.org/3/)

---

<div>
  <p>Made with ❤️ by <a href="https://github.com/w4t3rcs">w4t3rcs</a></p>
</div>
