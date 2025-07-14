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

@Aspect
@Component
public class SpelythonAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonResolver spelythonResolver;

    @Autowired
    public SpelythonAspect(PythonExecutor pythonExecutor, @Qualifier("spelythonResolver") PythonResolver spelythonResolver) {
        this.pythonExecutor = pythonExecutor;
        this.spelythonResolver = spelythonResolver;
    }

    @Before("@annotation(io.w4t3rcs.python.metadata.SpelythonBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, SpelythonBefore.class, pythonExecutor, SpelythonBefore::value,
                point -> AspectUtil.getMethodParameters(point, SpelythonParam.class, SpelythonParam::value),
                spelythonResolver);
    }

    @After("@annotation(io.w4t3rcs.python.metadata.SpelythonAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, SpelythonAfter.class, pythonExecutor, SpelythonAfter::value,
                point -> AspectUtil.getMethodParameters(point, SpelythonParam.class, SpelythonParam::value),
                spelythonResolver);
    }

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