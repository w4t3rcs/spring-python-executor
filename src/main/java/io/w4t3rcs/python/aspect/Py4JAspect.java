package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.metadata.Py4JAfter;
import io.w4t3rcs.python.metadata.Py4JBefore;
import io.w4t3rcs.python.service.PythonExecutor;
import io.w4t3rcs.python.service.PythonResolver;
import io.w4t3rcs.python.util.AspectUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
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
        AspectUtil.executePython(joinPoint, Py4JBefore.class, pythonExecutor,
                Py4JBefore::value,
                point -> null,
                py4JResolver::resolve);
    }

    @After("@annotation(io.w4t3rcs.python.metadata.Py4JAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.executePython(joinPoint, Py4JAfter.class, pythonExecutor,
                Py4JAfter::value,
                point -> null,
                py4JResolver::resolve);
    }
}
