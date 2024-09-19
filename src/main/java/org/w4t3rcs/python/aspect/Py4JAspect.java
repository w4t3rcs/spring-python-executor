package org.w4t3rcs.python.aspect;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.w4t3rcs.python.config.Py4JProperties;
import org.w4t3rcs.python.metadata.Py4JAfterMethod;
import org.w4t3rcs.python.metadata.Py4JBeforeMethod;
import org.w4t3rcs.python.service.PythonExecutor;
import org.w4t3rcs.python.util.JoinPointUtil;
import org.w4t3rcs.python.util.Py4JUtil;

import java.lang.reflect.Method;

@Data
@Aspect
@Component
@RequiredArgsConstructor
public class Py4JAspect {
    private final PythonExecutor pythonExecutor;
    private final Py4JProperties py4JProperties;

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
        if (py4JProperties.isAutoImport() && !script.endsWith(".py")) {
            pythonExecutor.execute(Py4JUtil.IMPORT_PY4J + script);
        } else {
            pythonExecutor.execute(script);
        }
    }
}
