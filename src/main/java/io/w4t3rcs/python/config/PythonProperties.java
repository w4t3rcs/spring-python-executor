package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("spring.python")
public record PythonProperties(@DefaultValue("python") String startCommand,
                               @DefaultValue("true") boolean isLoggable,
                               @DefaultValue("/python/") String path) {
}
