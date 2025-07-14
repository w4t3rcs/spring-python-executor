package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for result processing in Python scripts.
 * These properties control how result expressions are identified and processed within Python code.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.result".</p>
 */
@ConfigurationProperties("spring.python.result")
public record ResultProperties(@DefaultValue("o4java$\\{.+?}") String regex,
                               @DefaultValue("r4java$") String appearance,
                               @DefaultValue("8") int positionFromStart,
                               @DefaultValue("1") int positionFromEnd) {
}
