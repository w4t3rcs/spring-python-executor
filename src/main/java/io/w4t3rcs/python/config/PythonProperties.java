package io.w4t3rcs.python.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.python")
public class PythonProperties {
    private String startCommand = "python";
    private boolean isLoggable = true;
    private String path = "/python/";
}
