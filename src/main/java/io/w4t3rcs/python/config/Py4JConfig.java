package io.w4t3rcs.python.config;

import io.w4t3rcs.python.util.Py4JUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import py4j.GatewayServer;

@Configuration
@Conditional(Py4JCondition.class)
@EnableConfigurationProperties({Py4JProperties.class})
public class Py4JConfig {
    @Bean
    public GatewayServer py4JGatewayServer(Py4JProperties py4JProperties) {
        if (py4JProperties.isLoggable()) GatewayServer.turnAllLoggingOn();
        GatewayServer gatewayServer = Py4JUtil.createGatewayServer(null, py4JProperties);
        gatewayServer.start();
        return gatewayServer;
    }
}
