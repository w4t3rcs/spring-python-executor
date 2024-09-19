package org.w4t3rcs.python.dto;

import lombok.*;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.List;

@Data
public class Py4JEntryPoint {
    private List<BeanDefinition> availableBeanDefinitions;
}
