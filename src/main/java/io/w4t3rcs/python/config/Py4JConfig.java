package io.w4t3rcs.python.config;

import io.w4t3rcs.python.util.Py4JUtil;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py4j.GatewayServer;

@Configuration
@ConditionalOnProperty(name = "spring.python.py4j.enabled", havingValue = "true")
public class Py4JConfig {
    @SneakyThrows
    @Bean
    public GatewayServer py4JGatewayServer(Py4JProperties py4JProperties) {
        GatewayServer.turnAllLoggingOn();
        GatewayServer gatewayServer = Py4JUtil.createGatewayServer(null, py4JProperties);
        gatewayServer.start();
        return gatewayServer;
    }
}
