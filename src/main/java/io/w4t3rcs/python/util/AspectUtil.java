package io.w4t3rcs.python.util;

import io.w4t3rcs.python.service.PythonExecutor;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public class AspectUtil {
    public <T extends Annotation> void executePython(JoinPoint joinPoint, Class<? extends T> annotationClass, PythonExecutor pythonExecutor, Function<T, String> scriptGetter, Function<JoinPoint, Map<String, Object>> argumentsGetter, BiFunction<String, Map<String, Object>, String> scriptResolver) {
        Method method = getMethod(joinPoint);
        T annotation = method.getAnnotation(annotationClass);
        String script = scriptGetter.apply(annotation);
        Map<String, Object> arguments = argumentsGetter.apply(joinPoint);
        pythonExecutor.execute(scriptResolver.apply(script, arguments));
    }

    public Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    public Map<String, Object> getMethodParameters(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] objects = joinPoint.getArgs();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], objects[i]);
        }
        return map;
    }
}
