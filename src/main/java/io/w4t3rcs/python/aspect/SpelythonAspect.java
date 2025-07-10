package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.metadata.SpelythonAfter;
import io.w4t3rcs.python.metadata.SpelythonBefore;
import io.w4t3rcs.python.service.PythonExecutor;
import io.w4t3rcs.python.service.PythonResolver;
import io.w4t3rcs.python.util.AspectUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class SpelythonAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonResolver spelythonResolver;

    @SneakyThrows
    @Before("@annotation(io.w4t3rcs.python.metadata.SpelythonBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.executePython(joinPoint, SpelythonBefore.class, pythonExecutor, SpelythonBefore::value, AspectUtil::getMethodParameters, spelythonResolver::resolve);
    }

    @SneakyThrows
    @After("@annotation(io.w4t3rcs.python.metadata.SpelythonAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.executePython(joinPoint, SpelythonAfter.class, pythonExecutor, SpelythonAfter::value, AspectUtil::getMethodParameters, spelythonResolver::resolve);
    }

    @SneakyThrows
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.metadata.SpelythonAfter)", returning = "result")
    public void executeAfterReturningMethod(JoinPoint joinPoint, Object result) {
        AspectUtil.executePython(joinPoint, SpelythonAfter.class, pythonExecutor,
                SpelythonAfter::value,
                point -> {
                    Map<String, Object> arguments = AspectUtil.getMethodParameters(point);
                    arguments.put("result", result);
                    return arguments;
                },
                spelythonResolver::resolve);
    }
}
