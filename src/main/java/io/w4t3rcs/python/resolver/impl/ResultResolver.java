package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.config.ResultProperties;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.AbstractPythonResolver;

import java.util.Map;

/**
 * Resolver implementation that processes result expressions in Python scripts.
 * This resolver adds print statements to the script to capture and format the results
 * of expressions, making them available for retrieval after script execution.
 * 
 * <p>The resolver can process both inline scripts and scripts loaded from files.</p>
 */
public class ResultResolver extends AbstractPythonResolver {
    private final ResultProperties resultProperties;

    public ResultResolver(ResultProperties resultProperties, PythonFileHandler pythonFileHandler) {
        super(pythonFileHandler);
        this.resultProperties = resultProperties;
    }

    /**
     * Processes a script to find result expressions and wraps them in print statements.
     * 
     * <p>This method uses the configured regex pattern to find result expressions in the script
     * and wraps them in print statements with a specific appearance prefix to make them
     * identifiable in the output.</p>
     *
     * @param script The Python script content to process
     * @return The processed script with result expressions wrapped in print statements
     */
    @Override
    protected String handleResolve(String script, Map<String, Object> arguments) {
        return this.replaceScriptFragments(script, resultProperties.regex(),
                resultProperties.positionFromStart(), resultProperties.positionFromEnd(),
                (matcher, fragment) ->
                        "print('" + resultProperties.appearance() + "' + json.dumps(" + fragment + "))");
    }
}
