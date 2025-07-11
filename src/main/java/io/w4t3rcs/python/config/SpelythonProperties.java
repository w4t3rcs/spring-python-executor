package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("spring.python.spelython")
public record SpelythonProperties(@DefaultValue("spel\\{.+?}") String regex,
                                  @DefaultValue("#") String spelLocalVariableIndex,
                                  @DefaultValue("5") int spelPositionFromStart,
                                  @DefaultValue("1") int spelPositionFromEnd) {
}
