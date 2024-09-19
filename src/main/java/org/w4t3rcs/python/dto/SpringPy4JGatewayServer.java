package org.w4t3rcs.python.dto;

import org.w4t3rcs.python.config.Py4JProperties;
import py4j.CallbackClient;
import py4j.GatewayServer;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SpringPy4JGatewayServer extends GatewayServer {
    public SpringPy4JGatewayServer(SpringEntryPoint springEntryPoint, Py4JProperties py4JProperties) throws UnknownHostException {
        super(springEntryPoint, py4JProperties.getPort(), InetAddress.getByName(py4JProperties.getHost()), py4JProperties.getConnectTimeout(), py4JProperties.getReadTimeout(), null, new CallbackClient(py4JProperties.getPythonPort(), InetAddress.getByName(py4JProperties.getHost())), ServerSocketFactory.getDefault());
    }
}
