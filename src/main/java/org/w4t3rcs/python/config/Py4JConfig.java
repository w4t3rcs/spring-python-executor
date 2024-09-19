package org.w4t3rcs.python.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.w4t3rcs.python.dto.SpringEntryPoint;
import org.w4t3rcs.python.dto.SpringPy4JGatewayServer;

@Configuration
@ConditionalOnProperty(name = "spring.python.py4j.enabled", havingValue = "true")
public class Py4JConfig {
    @Bean
    public SpringEntryPoint springEntryPoint(ConfigurableListableBeanFactory beanFactory) {
        SpringEntryPoint springEntryPoint = new SpringEntryPoint();
        springEntryPoint.setElements(Py4JBeanFactoryUtil.getAllPy4JElements(beanFactory));
        return springEntryPoint;
    }

    @SneakyThrows
    @Bean
    public SpringPy4JGatewayServer py4JGatewayServer(SpringEntryPoint springEntryPoint, Py4JProperties py4JProperties) {
        SpringPy4JGatewayServer.turnAllLoggingOn();
        SpringPy4JGatewayServer gatewayServer = new SpringPy4JGatewayServer(springEntryPoint, py4JProperties);
        gatewayServer.start();
        return gatewayServer;
    }
}
