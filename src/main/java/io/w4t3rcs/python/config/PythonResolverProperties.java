package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for choosing needed {@link PythonResolver} implementation.
 * These properties allow the {@link PythonExecutor} bean to resolve the script properly before execution.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.resolver".</p>
 */
@ConfigurationProperties("spring.python.resolver")
public record PythonResolverProperties(@DefaultValue("result, spelython") String[] declared) {
}
