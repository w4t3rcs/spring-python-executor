package org.w4t3rcs.python.dto;

import lombok.Data;
import org.springframework.beans.factory.config.BeanDefinition;

@Data
public class Py4JElement {
    private String beanClassName;

    public Py4JElement(BeanDefinition beanDefinition) {
        this.beanClassName = beanDefinition.getBeanClassName();
    }
}
