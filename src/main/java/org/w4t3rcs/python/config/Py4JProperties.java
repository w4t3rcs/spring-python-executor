package org.w4t3rcs.python.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import py4j.GatewayServer;

@Data
@Component
@ConfigurationProperties("spring.python.py4j")
public class Py4JProperties {
    private String host = GatewayServer.DEFAULT_ADDRESS;
    private int port = GatewayServer.DEFAULT_PORT;
    private int pythonPort = GatewayServer.DEFAULT_PYTHON_PORT;
    private int connectTimeout = GatewayServer.DEFAULT_CONNECT_TIMEOUT;
    private int readTimeout = GatewayServer.DEFAULT_READ_TIMEOUT;
}
