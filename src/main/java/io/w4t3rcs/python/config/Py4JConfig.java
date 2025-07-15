package io.w4t3rcs.python.config;

import io.w4t3rcs.python.util.Py4JUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import py4j.GatewayServer;

/**
 * Configuration class for Py4J integration.
 * This class creates and starts a Py4J gateway server that enables
 * communication between Java and Python.
 * 
 * <p>The configuration is conditionally enabled based on the {@link Py4JCondition},
 * which checks if Py4J integration is enabled via properties or annotations.</p>
 */
@Configuration
@Conditional(Py4JCondition.class)
@EnableConfigurationProperties({Py4JProperties.class})
public class Py4JConfig {
    /**
     * Creates and starts a Py4J gateway server bean.
     * The gateway server allows Python code to access Java objects through the Py4J bridge.
     * 
     * <p>If logging is enabled in the properties, all Py4J logging is turned on.</p>
     *
     * @param py4JProperties Configuration properties for the Py4J gateway
     * @return A started GatewayServer instance
     */
    @Bean
    public GatewayServer py4JGatewayServer(Py4JProperties py4JProperties) {
        if (py4JProperties.loggable()) GatewayServer.turnAllLoggingOn();
        GatewayServer gatewayServer = Py4JUtil.createGatewayServer(null, py4JProperties);
        gatewayServer.start();
        return gatewayServer;
    }
}
