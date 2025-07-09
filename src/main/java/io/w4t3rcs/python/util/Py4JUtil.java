package io.w4t3rcs.python.util;

import io.w4t3rcs.python.config.Py4JProperties;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import py4j.CallbackClient;
import py4j.GatewayServer;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;

@UtilityClass
public class Py4JUtil {
    public final String IMPORT_PY4J = "from py4j.java_gateway import JavaGateway\ngateway = JavaGateway()\n";

    @SneakyThrows
    public GatewayServer createGatewayServer(Object entryPoint, Py4JProperties py4JProperties) {
        return new GatewayServer(entryPoint, py4JProperties.port(), InetAddress.getByName(py4JProperties.host()), py4JProperties.connectTimeout(), py4JProperties.readTimeout(), null, new CallbackClient(py4JProperties.pythonPort(), InetAddress.getByName(py4JProperties.host())), ServerSocketFactory.getDefault());
    }
}
