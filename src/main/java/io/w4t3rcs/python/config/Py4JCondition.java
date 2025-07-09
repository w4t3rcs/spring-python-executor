package io.w4t3rcs.python.config;

import io.w4t3rcs.python.metadata.EnablePy4J;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import py4j.GatewayServer;

import java.util.Arrays;

public class Py4JCondition implements Condition {
    public static final String ENABLED_PROPERTY = "spring.python.py4j.enabled";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            Class.forName(GatewayServer.class.getName(), false, this.getClass().getClassLoader());
            Environment environment = context.getEnvironment();
            String property = environment.getProperty(ENABLED_PROPERTY, "false");
            boolean isEnabledViaProperty = Boolean.parseBoolean(property);
            BeanDefinitionRegistry registry = context.getRegistry();
            boolean isEnabledViaAnnotation = Arrays.stream(registry.getBeanDefinitionNames())
                    .map(registry::getBeanDefinition)
                    .map(BeanDefinition::getSource)
                    .filter(StandardMethodMetadata.class::isInstance)
                    .map(StandardMethodMetadata.class::cast)
                    .anyMatch(methodMetadata -> methodMetadata.isAnnotated(EnablePy4J.class.getName()));            return (isEnabledViaProperty || isEnabledViaAnnotation);
        } catch (Exception e) {
            return false;
        }
    }
}
