package org.w4t3rcs.python.dto;

import lombok.*;

import java.util.List;

@Data
public class SpringEntryPoint {
    private List<Py4JElement> elements;
}
