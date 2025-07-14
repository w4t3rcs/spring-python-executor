package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.config.Py4JCondition;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.metadata.Py4JAfter;
import io.w4t3rcs.python.metadata.Py4JBefore;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.util.AspectUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Aspect that handles the execution of Python scripts through Py4J integration.
 * This aspect intercepts methods annotated with {@link Py4JBefore} and {@link Py4JAfter}
 * annotations and executes the specified Python scripts before or after the method execution.
 * 
 * <p>The aspect is conditionally enabled based on the {@link Py4JCondition}.</p>
 */
@Aspect
@Component
@Conditional(Py4JCondition.class)
public class Py4JAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonResolver py4JResolver;

    /**
     * Constructs a new Py4JAspect with the specified executor and resolver.
     *
     * @param pythonExecutor The executor for running Python scripts
     * @param py4JResolver The resolver for processing Python scripts with Py4J integration
     */
    @Autowired
    public Py4JAspect(PythonExecutor pythonExecutor, @Qualifier("py4JResolver") PythonResolver py4JResolver) {
        this.pythonExecutor = pythonExecutor;
        this.py4JResolver = py4JResolver;
    }

    /**
     * Executes Python scripts before methods annotated with {@link Py4JBefore}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation before the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.metadata.Py4JBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, Py4JBefore.class, pythonExecutor,
                Py4JBefore::value,
                point -> null,
                py4JResolver);
    }

    /**
     * Executes Python scripts after methods annotated with {@link Py4JAfter}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @After("@annotation(io.w4t3rcs.python.metadata.Py4JAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, Py4JAfter.class, pythonExecutor,
                Py4JAfter::value,
                point -> null,
                py4JResolver);
    }
}
