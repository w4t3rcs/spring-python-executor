package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for Spring Expression Language (SpEL) integration in Python scripts.
 * These properties control how SpEL expressions are identified and processed within Python code.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.spelython".</p>
 */
@ConfigurationProperties("spring.python.spelython")
public record SpelythonProperties(@DefaultValue("spel\\{.+?}") String regex,
                                  @DefaultValue("#") String spelLocalVariableIndex,
                                  @DefaultValue("5") int spelPositionFromStart,
                                  @DefaultValue("1") int spelPositionFromEnd) {
}
