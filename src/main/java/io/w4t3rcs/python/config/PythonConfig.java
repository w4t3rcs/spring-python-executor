package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Main configuration class for Python integration.
 * This class sets up the core infrastructure for executing Python scripts from Java.
 * 
 * <p>It enables:</p>
 * <ul>
 *   <li>Component scanning for all classes in the io.w4t3rcs.python package</li>
 *   <li>Aspect-oriented programming support for Python script execution via annotations</li>
 *   <li>Configuration properties for Python execution, SpEL integration, and result processing</li>
 * </ul>
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({
        PythonProperties.class,
        SpelythonProperties.class,
        ResultProperties.class
})
@ComponentScan("io.w4t3rcs.python")
public class PythonConfig {
}
