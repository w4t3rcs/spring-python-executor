package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.config.Py4JCondition;
import io.w4t3rcs.python.config.Py4JProperties;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.AbstractPythonResolver;

import java.util.Map;

/**
 * Resolver implementation that processes Python scripts for Py4J integration.
 * This resolver adds the necessary import statement to Python scripts to enable
 * communication between Java and Python using the Py4J bridge.
 * 
 * <p>The resolver can process both inline scripts and scripts loaded from files.</p>
 * 
 * <p>This resolver is conditionally enabled based on the {@link Py4JCondition}.</p>
 */
public class Py4JResolver extends AbstractPythonResolver {
    private final Py4JProperties py4JProperties;

    public Py4JResolver(Py4JProperties py4JProperties, PythonFileHandler pythonFileHandler) {
        super(pythonFileHandler);
        this.py4JProperties = py4JProperties;
    }

    @Override
    protected String handleResolve(String script, Map<String, Object> arguments) {
        return py4JProperties.importLine() + script;
    }
}
