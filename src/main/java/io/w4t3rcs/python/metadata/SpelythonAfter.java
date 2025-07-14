package io.w4t3rcs.python.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to execute a Python script with Spring Expression Language (SpEL) integration after a method.
 * 
 * <p>When a method is annotated with {@code SpelythonAfter}, the specified Python script
 * will be executed after the method completes. This annotation allows embedding SpEL expressions
 * in the Python script, which are evaluated at runtime and replaced with their values.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#64;SpelythonAfter("my_script.py")
 * public void myMethod() {
 *     // Method implementation
 * }
 * </pre>
 * 
 * <p>Example of a Python script with SpEL expressions:</p>
 * <pre>
 * # Access a bean property
 * value = spel{@myBean.property}
 * 
 * # Perform calculations
 * result = spel{2 + 2}
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SpelythonAfter {
    /**
     * The Python script to execute after the method.
     * This can be either the path to a Python file or the actual Python code.
     * The script can contain SpEL expressions enclosed in spel{...}.
     * 
     * @return the Python script or file path
     */
    String value();
}
