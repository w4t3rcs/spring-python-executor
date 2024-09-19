package org.w4t3rcs.python.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.w4t3rcs.python.dto.SpringEntryPoint;
import org.w4t3rcs.python.dto.SpringPy4JGatewayServer;

public class Py4JRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;
        AbstractBeanDefinition springEntryPointBean = BeanDefinitionBuilder.genericBeanDefinition(SpringEntryPoint.class)
                .addPropertyValue("elements", Py4JBeanFactoryUtil.getAllPy4JElements(beanFactory))
                .getBeanDefinition();
        registry.registerBeanDefinition("springEntryPoint", springEntryPointBean);
        AbstractBeanDefinition gatewayServerBean = BeanDefinitionBuilder.genericBeanDefinition(SpringPy4JGatewayServer.class)
                .addConstructorArgReference("springEntryPoint")
                .addConstructorArgReference("py4JProperties")
                .setInitMethodName("start")
                .getBeanDefinition();
        registry.registerBeanDefinition("py4JGatewayServer", gatewayServerBean);
    }
}
