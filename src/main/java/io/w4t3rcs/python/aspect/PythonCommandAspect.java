package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.metadata.PythonAfterMethod;
import io.w4t3rcs.python.metadata.PythonBeforeMethod;
import io.w4t3rcs.python.service.PythonExecutor;
import io.w4t3rcs.python.util.JoinPointUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class PythonCommandAspect {
    private final PythonExecutor pythonExecutor;

    @Before("@annotation(io.w4t3rcs.python.metadata.PythonBeforeMethod)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        PythonBeforeMethod pythonBeforeMethod = method.getAnnotation(PythonBeforeMethod.class);
        pythonExecutor.execute(pythonBeforeMethod.value());
    }

    @After("@annotation(io.w4t3rcs.python.metadata.PythonAfterMethod)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        PythonAfterMethod pythonAfterMethod = method.getAnnotation(PythonAfterMethod.class);
        pythonExecutor.execute(pythonAfterMethod.value());
    }
}
