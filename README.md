# Spring Python Executor

**Spring Python Executor** is a Spring Boot AOP-based handler for executing Python code before or after method calls. It leverages Spring AOP, Py4J, and ProcessBuilder to allow seamless integration of Python scripts with Java applications.

## Features

- **Aspect-Oriented Programming (AOP):** Execute Python scripts before or after Java method calls.
- **Py4J Integration:** Use Py4J to interact with Python code directly from Java, enabling deep integration.
- **ProcessBuilder Integration:** Execute external Python scripts using `ProcessBuilder` to handle Python processes in a Spring Boot environment.
- **Configuration Driven:** Flexible configuration using Spring Boot properties and annotations.
- **Customizable:** Extendable aspects to handle Python script execution based on your business logic.

## Directory Structure

```
org.w4t3rcs.python
├── aspect
│   ├── Py4JAspect                # Aspect to handle Py4J-based Python execution
│   ├── PythonCommandAspect       # Aspect for executing Python scripts using ProcessBuilder
│   ├── SpelythonAspect           # Aspect for executing SPeL + Python scripts using ProcessBuilder
├── config
│   ├── EnablePy4J                # Enable annotation for Py4J configuration
│   ├── Py4JConfig                # Py4J configuration class
│   ├── Py4JProperties            # Configuration properties for Py4J
│   ├── Py4JRegistrar             # Bean registrar for Py4J
│   ├── PythonConfig              # Python configuration class
├── metadata
│   ├── Py4JAfterMethod           # Metadata class for after method executions via Py4J
│   ├── Py4JBeforeMethod          # Metadata class for before method executions via Py4J
│   ├── PythonAfterMethod         # Metadata class for after method executions via Python script
│   ├── PythonBeforeMethod        # Metadata class for before method executions via Python script
│   ├── SpelythonAfterMethod      # Metadata class for after method executions via SpEL + Python script
│   ├── SpelythonBeforeMethod     # Metadata class for before method executions via SpEL + Python script
├── service
│   ├── PythonExecutor            # Service interface for executing Python scripts
│   ├── SpelythonResolver         # Service interface for resolving all SpEL from Python scripts
│   ├── impl
│   │───├── PythonExecutorImpl    # Implementation class for executing Python scripts
│   │   └── SpelythonResolverImpl # Implementation class for resolving all SpEL from Python scripts
├── util
│   ├── JoinPointUtil             # Utility methods for handling join points
│   ├── Py4JUtil                  # Utility methods for handling py4J
└───└── ScriptUtil                # Utility methods for handling scripts
```

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
spring.python.py4j.loggable=true
```

2. **Script Calls using AOP**:
   Use an AOP aspect (`PythonCommandAspect`) to inject Python script executions before or after specific method invocations.

   An example of Python execution:
```java
@PythonBeforeMethod("print('hello from python before java method')") //or @PythonAfterMethod("print('hello from python after java method')")
// Or @PythonBeforeMethod("example.py") or @PythonAfterMethod("example.py")
public void doSmth() {
   //Some business-logic
}
```
3. **Script Calls using `PythonExecutor`**:
   Use `PythonExecutor` to execute Python script.

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
      //Some business-logic
      pythonExecutor.execute("print('hello from python')"); //or pythonExecutor.execute("example.py");
      //Some business-logic
   }
}
```

- ### Spelython (SPeL + Python)
1. **Script Calls using AOP**:
   Use an AOP aspect (`SpelythonAspect`) to inject Python script executions before or after specific method invocations.

   An example of Python execution:
```java
@SpelythonBeforeMethod("print('${1 + 1}')") //or @SpelythonAfterMethod("print('${1 + 1}')")
// Or @SpelythonBeforeMethod("example.py") or @SpelythonAfterMethod("example.py")
public void doSmth() {
   //Some business-logic
}
```
2. **Script Calls using `SpelythonResolver` + `PythonExecutor`**:
   Use `SpelythonResolver` + `PythonExecutor` to execute Python script.

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
      //Some business-logic
      String script = "print('${1 + 1}')";
      String resolvedScript = spelythonResolver.resolve(script);
      pythonExecutor.execute(resolvedScript); //or pythonExecutor.execute("example.py");
      //Some business-logic
   }
}
```

- ### Py4J
1. **Enable Py4J in Your Spring Boot Application**:
   Add the `@EnablePy4J` annotation to your main class to enable Py4J support or `spring.python.py4j.enabled=true` to `application.properties` or `application.yml`:

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

3. **Script Calls**:
   Use an AOP aspect (`Py4JAspect`) to inject Py4J script executions before or after specific method invocations.

   An example of Python execution with Py4J if `spring.python.py4j.auto-import=true` where `gateway` is `JavaGateway` from Py4J:
```java
@Py4JBeforeMethod("print(gateway)") //or @Py4JAfterMethod("print('gateway')")
// Or @Py4JBeforeMethod("example.py") or @Py4JAfterMethod("example.py")
public void doSmth() {
   //Some business-logic
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
---