package io.w4t3rcs.python.resolver.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.config.SpelythonProperties;
import io.w4t3rcs.python.exception.SpelythonProcessingException;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.AbstractPythonResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * Resolver implementation that processes Spring Expression Language (SpEL) expressions
 * within Python scripts. This resolver allows embedding SpEL expressions in Python code
 * which are evaluated at runtime and replaced with their JSON representation.
 * 
 * <p>The resolver can process both inline scripts and scripts loaded from files.</p>
 */
public class SpelythonResolver extends AbstractPythonResolver {
    private final SpelythonProperties spelythonProperties;
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    public SpelythonResolver(SpelythonProperties spelythonProperties, PythonFileHandler pythonFileHandler, ApplicationContext context, ObjectMapper objectMapper) {
        super(pythonFileHandler);
        this.spelythonProperties = spelythonProperties;
        this.applicationContext = context;
        this.objectMapper = objectMapper;
    }

    /**
     * Processes a script to find and evaluate SpEL expressions, replacing them with their
     * JSON representation that can be used in Python.
     * 
     * <p>This method sets up a SpEL evaluation context with the provided arguments,
     * evaluates expressions found in the script using the configured regex pattern,
     * and converts the results to JSON format.</p>
     *
     * @param script The Python script content to process
     * @param arguments A map of variables to be made available in the SpEL evaluation context
     * @return The processed script with SpEL expressions replaced by their evaluated JSON values
     * @throws SpelythonProcessingException If there's an error processing the JSON result
     */
    @Override
    protected String handleResolve(String script, Map<String, Object> arguments) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (arguments != null && !arguments.isEmpty()) {
            arguments.forEach((key, value) ->
                    parser.parseExpression(spelythonProperties.spelLocalVariableIndex() + key)
                            .setValue(context, value));
        }
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        return this.replaceScriptFragments(script, spelythonProperties.regex(),
                spelythonProperties.spelPositionFromStart(), spelythonProperties.spelPositionFromEnd(),
                ((matcher, fragment) -> {
                    try {
                        Expression expression = parser.parseExpression(fragment);
                        Object result = expression.getValue(context, Object.class);
                        String jsonResult = objectMapper.writeValueAsString(result);
                        String jsonPythonObject = "'" + jsonResult + "'";
                        return "json.loads(" + jsonPythonObject.replace("\"", "\"\"") + ")";
                    } catch (JsonProcessingException e) {
                        throw new SpelythonProcessingException(e);
                    }
                }));
    }
}
