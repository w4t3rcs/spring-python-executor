package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("spring.python.spelython")
public record SpelythonProperties(@DefaultValue("spel\\{.+}") String regex) {
}
