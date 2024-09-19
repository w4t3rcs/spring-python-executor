package org.w4t3rcs.python.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.w4t3rcs.python.dto.Py4JEntryPoint;
import org.w4t3rcs.python.metadata.Py4JComponent;
import py4j.GatewayServer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "spring.python.py4j.enabled", havingValue = "true")
public class Py4JConfig {
    @Bean
    public Py4JEntryPoint py4JEntryPoint(ConfigurableListableBeanFactory beanFactory) {
        List<BeanDefinition> availableBeanDefinitions = new ArrayList<>();
        for (String definitionName : beanFactory.getBeanDefinitionNames()) {
            if (beanFactory.findAnnotationOnBean(definitionName, Py4JComponent.class) != null) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(definitionName);
                availableBeanDefinitions.add(beanDefinition);
            }
        }

        Py4JEntryPoint py4JEntryPoint = new Py4JEntryPoint();
        py4JEntryPoint.setAvailableBeanDefinitions(availableBeanDefinitions);
        return py4JEntryPoint;
    }

    @Bean
    public GatewayServer gatewayServer(Py4JEntryPoint py4JEntryPoint) {
        GatewayServer gatewayServer = new GatewayServer(py4JEntryPoint);
        gatewayServer.start();
        return gatewayServer;
    }
}
