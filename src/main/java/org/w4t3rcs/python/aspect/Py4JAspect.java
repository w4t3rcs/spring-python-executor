package org.w4t3rcs.python.aspect;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w4t3rcs.python.metadata.*;
import org.w4t3rcs.python.service.PythonExecutor;

import java.lang.reflect.Method;

@Data
@Aspect
@Component
@RequiredArgsConstructor
public class Py4JAspect {
    private final PythonExecutor pythonExecutor;
    @Value("${spring.python.py4j.auto-import}")
    private boolean isPy4JAutoImportEnabled;

    @Before("@annotation(org.w4t3rcs.python.metadata.Py4JBeforeMethod)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        Py4JBeforeMethod pythonBeforeMethod = method.getAnnotation(Py4JBeforeMethod.class);
        String script = pythonBeforeMethod.value();
        executeScript(script);
    }

    @After("@annotation(org.w4t3rcs.python.metadata.Py4JAfterMethod)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethodFromJoinPoint(joinPoint);
        Py4JAfterMethod pythonAfterMethod = method.getAnnotation(Py4JAfterMethod.class);
        String script = pythonAfterMethod.value();
        executeScript(script);
    }

    private void executeScript(String script) {
        if (isPy4JAutoImportEnabled && !script.endsWith(".py")) {
            pythonExecutor.execute(Py4JComponent.importPy4J + script);
        } else {
            pythonExecutor.execute(script);
        }
    }
}
