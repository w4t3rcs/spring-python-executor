package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.resolver.impl.ResultResolver;
import io.w4t3rcs.python.resolver.impl.SpelythonResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class for {@link PythonResolver}.
 * This class sets up the core infrastructure for {@link PythonResolver} bean declaration.
 */
@Configuration
@EnableConfigurationProperties(PythonResolverProperties.class)
public class PythonResolverConfig {
    @Bean
    @Conditional(ResultResolverCondition.class)
    public PythonResolver resultResolver(ResultProperties resultProperties, PythonFileHandler pythonFileHandler) {
        return new ResultResolver(resultProperties, pythonFileHandler);
    }

    @Bean
    @Conditional(SpelythonResolverCondition.class)
    public PythonResolver spelythonResolver(SpelythonProperties spelythonProperties, PythonFileHandler pythonFileHandler, ApplicationContext applicationContext, ObjectMapper objectMapper) {
        return new SpelythonResolver(spelythonProperties, pythonFileHandler, applicationContext, objectMapper);
    }
}