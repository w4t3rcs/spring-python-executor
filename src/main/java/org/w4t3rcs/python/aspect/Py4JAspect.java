package org.w4t3rcs.python.aspect;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.w4t3rcs.python.metadata.Py4JAfterMethod;
import org.w4t3rcs.python.metadata.Py4JBeforeMethod;
import org.w4t3rcs.python.service.PythonCompletionResolver;
import org.w4t3rcs.python.service.PythonExecutor;
import org.w4t3rcs.python.util.JoinPointUtil;

import java.lang.reflect.Method;

@Data
@Aspect
@Component
@RequiredArgsConstructor
public class Py4JAspect {
    private final PythonExecutor pythonExecutor;
    private final PythonCompletionResolver py4JResolver;

    @Before("@annotation(org.w4t3rcs.python.metadata.Py4JBeforeMethod)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        Py4JBeforeMethod pythonBeforeMethod = method.getAnnotation(Py4JBeforeMethod.class);
        String script = pythonBeforeMethod.value();
        pythonExecutor.execute(py4JResolver.resolve(script));
    }

    @After("@annotation(org.w4t3rcs.python.metadata.Py4JAfterMethod)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        Py4JAfterMethod pythonAfterMethod = method.getAnnotation(Py4JAfterMethod.class);
        String script = pythonAfterMethod.value();
        pythonExecutor.execute(py4JResolver.resolve(script));
    }
}
