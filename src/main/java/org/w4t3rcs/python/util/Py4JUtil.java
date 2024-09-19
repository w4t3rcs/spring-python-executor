package org.w4t3rcs.python.util;

import lombok.SneakyThrows;
import org.w4t3rcs.python.config.Py4JProperties;
import py4j.CallbackClient;
import py4j.GatewayServer;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;

public class Py4JUtil {
    public static final String IMPORT_PY4J = "from py4j.java_gateway import JavaGateway\ngateway = JavaGateway()\nentry_point = gateway.entry_point";

    @SneakyThrows
    public static GatewayServer createGatewayServer(Object entryPoint, Py4JProperties py4JProperties) {
        return new GatewayServer(entryPoint, py4JProperties.getPort(), InetAddress.getByName(py4JProperties.getHost()), py4JProperties.getConnectTimeout(), py4JProperties.getReadTimeout(), null, new CallbackClient(py4JProperties.getPythonPort(), InetAddress.getByName(py4JProperties.getHost())), ServerSocketFactory.getDefault());
    }
}
