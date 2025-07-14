package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.metadata.SpelythonAfter;
import io.w4t3rcs.python.metadata.SpelythonBefore;
import io.w4t3rcs.python.metadata.SpelythonParam;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.util.AspectUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Aspect that handles the execution of Python scripts with Spring Expression Language (SpEL) integration.
 * This aspect intercepts methods annotated with {@link SpelythonBefore} and {@link SpelythonAfter}
 * annotations and executes the specified Python scripts before or after the method execution.
 * 
 * <p>The aspect uses the SpelythonResolver to process SpEL expressions in the Python scripts,
 * allowing the scripts to access Spring beans, method parameters, and the method's return value.</p>
 */
@Aspect
@Component
public class SpelythonAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonResolver spelythonResolver;

    /**
     * Constructs a new SpelythonAspect with the specified executor and resolver.
     *
     * @param pythonExecutor The executor for running Python scripts
     * @param spelythonResolver The resolver for processing SpEL expressions in Python scripts
     */
    @Autowired
    public SpelythonAspect(PythonExecutor pythonExecutor, @Qualifier("spelythonResolver") PythonResolver spelythonResolver) {
        this.pythonExecutor = pythonExecutor;
        this.spelythonResolver = spelythonResolver;
    }

    /**
     * Executes Python scripts before methods annotated with {@link SpelythonBefore}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation before the method execution, making method parameters available
     * to SpEL expressions in the script.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.metadata.SpelythonBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, SpelythonBefore.class, pythonExecutor, SpelythonBefore::value,
                point -> AspectUtil.getMethodParameters(point, SpelythonParam.class, SpelythonParam::value),
                spelythonResolver);
    }

    /**
     * Executes Python scripts after methods annotated with {@link SpelythonAfter}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method execution, making method parameters available
     * to SpEL expressions in the script.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @After("@annotation(io.w4t3rcs.python.metadata.SpelythonAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, SpelythonAfter.class, pythonExecutor, SpelythonAfter::value,
                point -> AspectUtil.getMethodParameters(point, SpelythonParam.class, SpelythonParam::value),
                spelythonResolver);
    }

    /**
     * Executes Python scripts after methods annotated with {@link SpelythonAfter} return a result.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method returns, making method parameters and the return value
     * available to SpEL expressions in the script.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param result The value returned by the method
     */
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.metadata.SpelythonAfter)", returning = "result")
    public void executeAfterReturningMethod(JoinPoint joinPoint, Object result) {
        AspectUtil.handlePythonAnnotation(joinPoint, SpelythonAfter.class, pythonExecutor,
                SpelythonAfter::value,
                point -> {
                    Map<String, Object> arguments = AspectUtil.getMethodParameters(point, SpelythonParam.class, SpelythonParam::value);
                    arguments.put("result", result);
                    return arguments;
                },
                spelythonResolver);
    }
}