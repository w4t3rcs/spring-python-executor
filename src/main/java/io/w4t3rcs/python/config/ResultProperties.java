package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("spring.python.result")
public record ResultProperties(@DefaultValue("o4java$\\{.+?}") String regex,
                               @DefaultValue("r4java$") String appearance,
                               @DefaultValue("5") int positionFromStart,
                               @DefaultValue("1") int positionFromEnd) {
}
