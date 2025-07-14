package io.w4t3rcs.python.util;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class AspectUtil {
    public <T extends Annotation> void handlePythonAnnotation(JoinPoint joinPoint, Class<? extends T> annotationClass, PythonExecutor pythonExecutor, Function<T, String> scriptGetter, Function<JoinPoint, Map<String, Object>> argumentsGetter, PythonResolver... pythonResolvers) {
        Method method = getMethod(joinPoint);
        T annotation = method.getAnnotation(annotationClass);
        String script = scriptGetter.apply(annotation);
        Map<String, Object> arguments = argumentsGetter.apply(joinPoint);
        PythonUtil.executeScript(script, null, pythonExecutor, arguments, pythonResolvers);
    }

    public Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    public <T extends Annotation> Map<String, Object> getMethodParameters(JoinPoint joinPoint, Class<? extends T> annotationClass, Function<T, String> annotatedNameGetter) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] objects = joinPoint.getArgs();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(annotationClass)) {
                T annotation = parameter.getAnnotation(annotationClass);
                String value = annotatedNameGetter.apply(annotation);
                map.put(value, objects[i]);
            } else {
                String parameterName = parameter.getName();
                map.put(parameterName, objects[i]);
            }
        }

        return map;
    }
}
