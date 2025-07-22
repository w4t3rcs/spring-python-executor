package io.w4t3rcs.python.config;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ResultResolverCondition extends AbstractResolverCondition {
    private static final String PROPERTY_VALUE = "result";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return this.matchesByProperty(context, PROPERTY_VALUE);
    }
}
