package io.w4t3rcs.python.util;

import io.w4t3rcs.python.config.Py4JProperties;
import io.w4t3rcs.python.exception.GatewayCreationException;
import lombok.experimental.UtilityClass;
import py4j.CallbackClient;
import py4j.GatewayServer;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

@UtilityClass
public class Py4JUtil {
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
