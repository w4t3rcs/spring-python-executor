package org.w4t3rcs.python.metadata;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Py4JComponent {
    String importPy4J = "from py4j.java_gateway import JavaGateway\ngateway = JavaGateway()\nentry_point = gateway.entry_point\n";

    @AliasFor(annotation = Component.class)
    String value() default "";
}
