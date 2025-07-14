package io.w4t3rcs.python.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify a name for a method parameter to be used in SpEL expressions in Python scripts.
 * 
 * <p>When a parameter is annotated with {@code SpelythonParam}, the specified name can be used
 * to reference the parameter's value in SpEL expressions within Python scripts executed by
 * {@link SpelythonBefore} or {@link SpelythonAfter} annotations.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#64;SpelythonAfter("my_script.py")
 * public void myMethod(@SpelythonParam("userId") Long id, @SpelythonParam("userName") String name) {
 *     // Method implementation
 * }
 * </pre>
 * 
 * <p>In the Python script, the parameters can be accessed using SpEL expressions:</p>
 * <pre>
 * # Access the userId parameter
 * user_id = spel{#userId}
 * 
 * # Access the userName parameter
 * user_name = spel{#userName}
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SpelythonParam {
    /**
     * The name to use for the parameter in SpEL expressions.
     * If not specified, the parameter's actual name will be used.
     * 
     * @return the name for the parameter
     */
    String value();
}
