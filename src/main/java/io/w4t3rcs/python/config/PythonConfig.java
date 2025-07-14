package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({PythonProperties.class, SpelythonProperties.class, ResultProperties.class})
@ComponentScan("io.w4t3rcs.python")
public class PythonConfig {
}
