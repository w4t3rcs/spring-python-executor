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

/**
 * Utility class providing helper methods for aspect-oriented programming with Python integration.
 * This class contains methods for handling Python annotations on methods, extracting method
 * information from join points, and processing method parameters.
 */
@UtilityClass
public class AspectUtil {
    /**
     * Handles a Python annotation on a method by executing the associated Python script.
     * This method extracts the annotation from the method, gets the script and arguments,
     * and executes the script using the provided executor and resolvers.
     *
     * @param <T> The type of annotation
     * @param joinPoint The join point representing the intercepted method call
     * @param annotationClass The class of the annotation to look for
     * @param pythonExecutor The executor responsible for running Python scripts
     * @param scriptGetter A function that extracts the script path or content from the annotation
     * @param argumentsGetter A function that extracts arguments from the join point
     * @param pythonResolvers Variable number of resolvers to process the script before execution
     */
    public <T extends Annotation> void handlePythonAnnotation(JoinPoint joinPoint, Class<? extends T> annotationClass, PythonExecutor pythonExecutor, Function<T, String> scriptGetter, Function<JoinPoint, Map<String, Object>> argumentsGetter, PythonResolver... pythonResolvers) {
        Method method = getMethod(joinPoint);
        T annotation = method.getAnnotation(annotationClass);
        String script = scriptGetter.apply(annotation);
        Map<String, Object> arguments = argumentsGetter.apply(joinPoint);
        PythonUtil.executeScript(script, null, pythonExecutor, arguments, pythonResolvers);
    }

    /**
     * Gets the Method object from a JoinPoint.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @return The Method object representing the intercepted method
     */
    public Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    /**
     * Extracts method parameters from a join point and creates a map of parameter names to values.
     * If a parameter is annotated with the specified annotation, the name from the annotation is used.
     * Otherwise, the parameter's actual name is used.
     *
     * @param <T> The type of annotation
     * @param joinPoint The join point representing the intercepted method call
     * @param annotationClass The class of the annotation to look for on parameters
     * @param annotatedNameGetter A function that extracts the parameter name from the annotation
     * @return A map of parameter names to their values
     */
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
