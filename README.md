# Spring Python Executor

**Spring Python Executor** is a Spring Boot AOP-based handler for executing Python code before or after method calls. It leverages Spring AOP, Py4J, and ProcessBuilder to allow seamless integration of Python scripts with Java applications.

| **Python Executor** | **Spring Boot** |
|---------------------|-----------------|
| 1.1.0               | 3.3.3           |
| 1.0.0               | 3.3.3           |

```
<dependency>
   <groupId>io.github.w4t3rcs</groupId>
   <artifactId>spring-boot-python-executor</artifactId>
   <version>1.1.0</version>
</dependency>
```

## Features
- **Aspect-Oriented Programming (AOP):** Execute Python scripts before or after Java method calls.
- **Py4J Integration:** Use Py4J to interact with Python code directly from Java, enabling deep integration.
- **ProcessBuilder Integration:** Execute external Python scripts using `ProcessBuilder` to handle Python processes in a Spring Boot environment.
- **Configuration Driven:** Flexible configuration using Spring Boot properties and annotations.
- **Customizable:** Extendable aspects to handle Python script execution based on your business logic.

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven** (for building the project)
- **Python 3.x** (for executing Python scripts)
- **Py4J** (for interacting between Java and Python)

### Usage

- ### Python
1. **Configure Python Properties**:
    Add necessary Python configuration in `application.properties` or `application.yml`:

   Autoconfiguration for Python:
```properties
spring.python.start-command=python
```

2. **Script Calls using AOP**:
   Use an AOP aspect (`PythonCommandAspect`) to inject Python script executions before or after specific method invocations.

   An example of Python execution:
```java
@PythonBefore("print('hello from python before java method')") //or @PythonAfter("print('hello from python after java method')")
// Or @PythonBefore("example.py") or @PythonAfter("example.py")
public void doSmth() {
   //Some business logic
}
```
3. **Script Calls using `PythonExecutor`**:
   Use `PythonExecutor` to execute a Python script.

   An example of Python execution:

```java
@Service
public class Example {
   private final PythonExecutor pythonExecutor;

   @Autowired
   public Example(PythonExecutor pythonExecutor) {
       this.pythonExecutor = pythonExecutor;
   }
   
   public void doSmth() {
      //Some business logic
      pythonExecutor.execute("print('hello from python')"); //or pythonExecutor.execute("example.py");
      //Some business logic
   }
}
```

- ### Spelython (SPeL + Python)
   Script will contain `import json` at the beginning of it, and also for executing SpEL you should use `spel{...}`.
   After `SpelythonResolver` successfully manipulates SpEL expressions, you will be able to handle results using a dictionary.
1. **Script Calls using AOP**:
   Use an AOP aspect (`SpelythonAspect`) to inject Python script executions before or after specific method invocations.

   An example of SpEL injection using `spel{...}`:
```java
@SpelythonBefored("print(spel{@pythonProperties}[startCommand])") //or @SpelythonAfter("print(spel{@pythonProperties}[startCommand])")
// Or @SpelythonBefore("example.py") or @SpelythonAfter("example.py")
public void doSmth() {
   //Some business logic
}
```
2. **Script Calls using `SpelythonResolver` + `PythonExecutor`**:
   Use `SpelythonResolver` + `PythonExecutor` to execute a Python script.

   An example of Python execution:

```java
@Service
public class Example {
   private final PythonExecutor pythonExecutor;
   private final SpelythonResolver spelythonResolver;

   @Autowired
   public Example(PythonExecutor pythonExecutor, SpelythonResolver spelythonResolver) {
      this.pythonExecutor = pythonExecutor;
      this.spelythonResolver = spelythonResolver;
   }

   public void doSmth() {
      //Some business logic
      String script = "print(spel{@pythonProperties}[startCommand])";
      String resolvedScript = spelythonResolver.resolve(script);
      pythonExecutor.execute(resolvedScript); //or pythonExecutor.execute("example.py");
      //Some business logic
   }
}
```

- ### Py4J
1. **Enable Py4J in Your Spring Boot Application**:
   Add the `@EnablePy4J` annotation to your main class to enable Py4J support, or `spring.python.py4j.enabled=true` to `application.properties` or `application.yml`:

   ```java
   @EnablePy4J
   @SpringBootApplication
   public class Application {
       public static void main(String[] args) {
           SpringApplication.run(Application.class, args);
       }
   }
   ```

2.  **Configure Py4J Properties**:
   Add necessary Py4J configuration in `application.properties` or `application.yml`:

   Autoconfiguration for Py4J:
   ```properties
   spring.python.py4j.enabled=false
   spring.python.py4j.auto-import=true
   spring.python.py4j.host=127.0.0.1
   spring.python.py4j.port=25333
   spring.python.py4j.python-port=25334
   spring.python.py4j.connect-timeout=0
   spring.python.py4j.read-timeout=0
   ```

3. **Script Calls using AOP**:
   Use an AOP aspect (`Py4JAspect`) to inject Py4J script executions before or after specific method invocations.

   An example of Python execution with Py4J if `spring.python.py4j.auto-import=true` where `gateway` is `JavaGateway` from Py4J:
```java
@Py4JBefore("print(gateway)") //or @Py4JAfter("print(gateway)")
// Or @Py4JBefore("example.py") or @Py4JAfter("example.py")
public void doSmth() {
   //Some business logic
}
```

4. **Script Calls using `Py4JResolver` + `PythonExecutor`**:
   Use `Py4JResolver` + `PythonExecutor` to execute a Python script.

   An example of Python execution:

```java
@Service
public class Example {
   private final PythonExecutor pythonExecutor;
   private final Py4JResolver py4JResolver;

   @Autowired
   public Example(PythonExecutor pythonExecutor, Py4JResolver py4JResolver) {
      this.pythonExecutor = pythonExecutor;
      this.py4JResolver = py4JResolver;
   }

   public void doSmth() {
      //Some business logic
      String script = "print(gateway)";
      String resolvedScript = py4JResolver.resolve(script);
      pythonExecutor.execute(resolvedScript); //or pythonExecutor.execute("example.py");
      //Some business logic
   }
}
```

### License
This project is licensed under the Apache License 2.0 - see the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0.txt) file for details.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

### Developer
- **w4t3rcs** - [w4t3rofficial@gmail.com](mailto:w4t3rofficial@gmail.com)

## Links
- [GitHub Repository](https://github.com/w4t3rcs/spring-python-executor)
- [Py4J Documentation](https://www.py4j.org/)
- [SpEL Documentation](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html)
---
