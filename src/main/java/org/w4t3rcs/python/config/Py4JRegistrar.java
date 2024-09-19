package org.w4t3rcs.python.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.w4t3rcs.python.dto.Py4JEntryPoint;
import org.w4t3rcs.python.metadata.Py4JComponent;
import py4j.GatewayServer;

import java.util.ArrayList;
import java.util.List;

public class Py4JRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;
        List<BeanDefinition> availableBeanDefinitions = new ArrayList<>();
        for (String definitionName : beanFactory.getBeanDefinitionNames()) {
            if (beanFactory.findAnnotationOnBean(definitionName, Py4JComponent.class) != null) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(definitionName);
                availableBeanDefinitions.add(beanDefinition);
            }
        }

        AbstractBeanDefinition py4JEntryPointBean = BeanDefinitionBuilder.genericBeanDefinition(Py4JEntryPoint.class)
                .addPropertyValue("availableBeanDefinitions", availableBeanDefinitions)
                .getBeanDefinition();
        registry.registerBeanDefinition("py4JEntryPoint", py4JEntryPointBean);
        AbstractBeanDefinition gatewayServerBean = BeanDefinitionBuilder.genericBeanDefinition(GatewayServer.class)
                .addConstructorArgReference("py4JEntryPoint")
                .setInitMethodName("start")
                .getBeanDefinition();
        registry.registerBeanDefinition("py4JGatewayServer", gatewayServerBean);
    }
}
