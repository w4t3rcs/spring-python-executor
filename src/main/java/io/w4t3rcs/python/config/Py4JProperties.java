package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import py4j.GatewayServer;

@ConfigurationProperties("spring.python.py4j")
public record Py4JProperties(@DefaultValue("true") boolean autoImport,
                             @DefaultValue(GatewayServer.DEFAULT_ADDRESS) String host,
                             @DefaultValue("25333") int port,
                             @DefaultValue("25334") int pythonPort,
                             @DefaultValue("0") int connectTimeout,
                             @DefaultValue("0") int readTimeout) {
}
