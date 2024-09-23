package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.metadata.SpelythonAfterMethod;
import io.w4t3rcs.python.metadata.SpelythonBeforeMethod;
import io.w4t3rcs.python.service.PythonCompletionResolver;
import io.w4t3rcs.python.service.PythonExecutor;
import io.w4t3rcs.python.util.JoinPointUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Data
@Aspect
@Component
@RequiredArgsConstructor
public class SpelythonAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonCompletionResolver spelythonResolver;

    @Before("@annotation(io.w4t3rcs.python.metadata.SpelythonBeforeMethod)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        SpelythonBeforeMethod pythonBeforeMethod = method.getAnnotation(SpelythonBeforeMethod.class);
        String script = pythonBeforeMethod.value();
        pythonExecutor.execute(spelythonResolver.resolve(script));
    }

    @SneakyThrows
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.metadata.SpelythonAfterMethod)", returning = "result")
    public void executeAfterMethod(JoinPoint joinPoint, Object result) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        SpelythonAfterMethod pythonAfterMethod = method.getAnnotation(SpelythonAfterMethod.class);
        String script = pythonAfterMethod.value();
        pythonExecutor.execute(spelythonResolver.resolve(script, result));
    }
}
