package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.metadata.PythonAfter;
import io.w4t3rcs.python.metadata.PythonBefore;
import io.w4t3rcs.python.service.PythonExecutor;
import io.w4t3rcs.python.util.AspectUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PythonCommandAspect {
    private final PythonExecutor pythonExecutor;

    @Before("@annotation(io.w4t3rcs.python.metadata.PythonBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.executePython(joinPoint, PythonBefore.class, pythonExecutor,
                PythonBefore::value,
                point -> null,
                (script, arguments) -> script);
    }

    @After("@annotation(io.w4t3rcs.python.metadata.PythonAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.executePython(joinPoint, PythonAfter.class, pythonExecutor,
                PythonAfter::value,
                point -> null,
                (script, arguments) -> script);
    }
}
