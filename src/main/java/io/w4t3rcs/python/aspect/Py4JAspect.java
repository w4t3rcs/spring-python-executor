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

@Aspect
@Component
@Conditional(Py4JCondition.class)
public class Py4JAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonResolver py4JResolver;

    @Autowired
    public Py4JAspect(PythonExecutor pythonExecutor, @Qualifier("py4JResolver") PythonResolver py4JResolver) {
        this.pythonExecutor = pythonExecutor;
        this.py4JResolver = py4JResolver;
    }

    @Before("@annotation(io.w4t3rcs.python.metadata.Py4JBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, Py4JBefore.class, pythonExecutor,
                Py4JBefore::value,
                point -> null,
                py4JResolver);
    }

    @After("@annotation(io.w4t3rcs.python.metadata.Py4JAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, Py4JAfter.class, pythonExecutor,
                Py4JAfter::value,
                point -> null,
                py4JResolver);
    }
}
