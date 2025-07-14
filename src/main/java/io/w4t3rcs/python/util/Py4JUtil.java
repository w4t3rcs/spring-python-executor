package io.w4t3rcs.python.util;

import io.w4t3rcs.python.config.Py4JProperties;
import io.w4t3rcs.python.exception.GatewayCreationException;
import lombok.experimental.UtilityClass;
import py4j.CallbackClient;
import py4j.GatewayServer;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility class providing helper methods for Py4J integration.
 * This class contains methods for creating and configuring Py4J gateway servers,
 * which enable communication between Java and Python.
 */
@UtilityClass
public class Py4JUtil {
    /**
     * Creates a new Py4J gateway server with the specified entry point and properties.
     * The gateway server allows Python code to access Java objects through the Py4J bridge.
     *
     * @param entryPoint The Java object that will be the entry point for Python code
     * @param py4JProperties Configuration properties for the Py4J gateway
     * @return A configured GatewayServer instance ready to be started
     * @throws GatewayCreationException if there's an error creating the gateway server,
     *         such as an unknown host
     */
    public GatewayServer createGatewayServer(Object entryPoint, Py4JProperties py4JProperties) {
        try {
            return new GatewayServer(entryPoint,
                    py4JProperties.port(),
                    InetAddress.getByName(py4JProperties.host()),
                    py4JProperties.connectTimeout(),
                    py4JProperties.readTimeout(),
                    null,
                    new CallbackClient(py4JProperties.pythonPort(), InetAddress.getByName(py4JProperties.host())),
                    ServerSocketFactory.getDefault());
        } catch (UnknownHostException e) {
            throw new GatewayCreationException(e);
        }
    }
}
