package io.w4t3rcs.python.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to execute a Python script after a method using Py4J integration.
 * 
 * <p>When a method is annotated with {@code Py4JAfter}, the specified Python script
 * will be executed after the method completes, using the Py4J bridge to allow
 * the Python script to interact with Java objects.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#64;Py4JAfter("my_script.py")
 * public void myMethod() {
 *     // Method implementation
 * }
 * </pre>
 * 
 * <p>Note that Py4J integration must be enabled in the application for this
 * annotation to have any effect. This can be done using the {@link EnablePy4J}
 * annotation or by setting the "spring.python.py4j.enabled" property to "true".</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Py4JAfter {
    /**
     * The Python script to execute after the method.
     * This can be either the path to a Python file or the actual Python code.
     * 
     * @return the Python script or file path
     */
    String value();
}
