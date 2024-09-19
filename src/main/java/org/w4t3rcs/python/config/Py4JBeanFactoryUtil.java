package org.w4t3rcs.python.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.w4t3rcs.python.dto.Py4JElement;
import org.w4t3rcs.python.metadata.Py4JComponent;

import java.util.ArrayList;
import java.util.List;

public class Py4JBeanFactoryUtil {
    public static List<Py4JElement> getAllPy4JElements(ConfigurableListableBeanFactory beanFactory) {
        List<Py4JElement> elements = new ArrayList<>();
        for (String definitionName : beanFactory.getBeanDefinitionNames()) {
            if (beanFactory.findAnnotationOnBean(definitionName, Py4JComponent.class) != null) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(definitionName);
                Py4JElement element = new Py4JElement(beanDefinition);
                elements.add(element);
            }
        }

        return elements;
    }
}
