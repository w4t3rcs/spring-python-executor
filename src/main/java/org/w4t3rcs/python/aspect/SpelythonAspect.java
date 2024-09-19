package org.w4t3rcs.python.aspect;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.w4t3rcs.python.metadata.SpelythonAfterMethod;
import org.w4t3rcs.python.metadata.SpelythonBeforeMethod;
import org.w4t3rcs.python.service.PythonCompletionResolver;
import org.w4t3rcs.python.service.PythonExecutor;
import org.w4t3rcs.python.util.JoinPointUtil;

import java.lang.reflect.Method;

@Data
@Aspect
@Component
@RequiredArgsConstructor
public class SpelythonAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonCompletionResolver spelythonResolver;

    @Before("@annotation(org.w4t3rcs.python.metadata.SpelythonBeforeMethod)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        SpelythonBeforeMethod pythonBeforeMethod = method.getAnnotation(SpelythonBeforeMethod.class);
        String script = pythonBeforeMethod.value();
        pythonExecutor.execute(spelythonResolver.resolve(script));
    }

    @After("@annotation(org.w4t3rcs.python.metadata.SpelythonAfterMethod)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        SpelythonAfterMethod pythonAfterMethod = method.getAnnotation(SpelythonAfterMethod.class);
        String script = pythonAfterMethod.value();
        pythonExecutor.execute(spelythonResolver.resolve(script));
    }
}
