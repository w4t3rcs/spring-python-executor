package org.w4t3rcs.python.config;

import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import py4j.GatewayServer;

public class Py4JRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        AbstractBeanDefinition gatewayServerBean = BeanDefinitionBuilder.genericBeanDefinition(GatewayServer.class)
                .setInitMethodName("start")
                .getBeanDefinition();
        registry.registerBeanDefinition("py4JGatewayServer", gatewayServerBean);
    }
}
