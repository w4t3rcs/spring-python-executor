package org.w4t3rcs.python.metadata;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Py4JAfterMethod {
    String value() default "";
}
