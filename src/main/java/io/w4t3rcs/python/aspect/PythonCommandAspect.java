package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.metadata.PythonAfter;
import io.w4t3rcs.python.metadata.PythonBefore;
import io.w4t3rcs.python.util.AspectUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspect that handles the execution of Python scripts through annotations.
 * This aspect intercepts methods annotated with {@link PythonBefore} and {@link PythonAfter}
 * annotations and executes the specified Python scripts before or after the method execution.
 * 
 * <p>Unlike the {@link Py4JAspect}, this aspect does not use the Py4J bridge, so the Python
 * scripts cannot directly interact with Java objects.</p>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PythonCommandAspect {
    private final PythonExecutor pythonExecutor;

    /**
     * Executes Python scripts before methods annotated with {@link PythonBefore}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation before the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.metadata.PythonBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, PythonBefore.class, pythonExecutor,
                PythonBefore::value,
                point -> null);
    }

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfter}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @After("@annotation(io.w4t3rcs.python.metadata.PythonAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, PythonAfter.class, pythonExecutor,
                PythonAfter::value,
                point -> null);
    }
}
