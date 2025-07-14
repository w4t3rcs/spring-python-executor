package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.config.Py4JCondition;
import io.w4t3rcs.python.config.Py4JProperties;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

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
@Service("py4JResolver")
@Conditional(Py4JCondition.class)
@RequiredArgsConstructor
public class Py4JResolver implements PythonResolver {
    private final PythonFileHandler pythonFileHandler;
    private final Py4JProperties py4JProperties;

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        if (pythonFileHandler.isPythonFile(script)) {
            return py4JProperties.importLine() + pythonFileHandler.readScriptBodyFromFile(script);
        } else {
            return py4JProperties.importLine() + script;
        }
    }
}
