package io.w4t3rcs.python.util;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing helper methods for Python script processing and execution.
 * This class contains methods for executing Python scripts with resolvers and
 * manipulating script content using regular expressions.
 */
@UtilityClass
public class PythonUtil {
    /**
     * Constant for a Python JSON import statement.
     * This string is automatically added to Python scripts that need JSON functionality.
     */
    public static final String IMPORT_JSON = "import json\n";

    /**
     * Executes a Python script after processing it through a series of resolvers.
     *
     * @param <R> The type of result expected from the script execution
     * @param script The Python script to execute
     * @param resultClass The class representing the expected result type
     * @param pythonExecutor The executor responsible for running the Python script
     * @param arguments A map of arguments to be used by resolvers
     * @param pythonResolvers Variable number of resolvers to process the script before execution
     * @return The result of the script execution, cast to the specified result class
     */
    public <R> R executeScript(String script, Class<? extends R> resultClass, PythonExecutor pythonExecutor, Map<String, Object> arguments, PythonResolver... pythonResolvers) {
        String result = script;
        for (PythonResolver pythonResolver : pythonResolvers) {
            result = pythonResolver.resolve(result, arguments);
        }

        return pythonExecutor.execute(result, resultClass);
    }

    /**
     * Replaces fragments in a Python script that match a given regular expression pattern.
     * This method ensures the script includes the JSON import statement and applies
     * a custom transformation to matched fragments using the provided bodyMapper function.
     *
     * @param script The Python script to process
     * @param regex The regular expression pattern to match script fragments
     * @param positionFromStart The number of characters to skip from the start of the matched group
     * @param positionFromEnd The number of characters to skip from the end of the matched group
     * @param bodyMapper A function that transforms the matched expression string
     * @return The processed script with replaced fragments
     */
    public String replaceScriptFragments(String script, String regex, int positionFromStart, int positionFromEnd, BiFunction<Matcher, String, String> bodyMapper) {
        StringBuilder resolvedScript = new StringBuilder(script);
        if (!script.contains(IMPORT_JSON)) resolvedScript.insert(0, IMPORT_JSON);
        Pattern spelPattern = Pattern.compile(regex);
        Matcher matcher = spelPattern.matcher(resolvedScript);
        if (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(positionFromStart, group.length() - positionFromEnd);
            String result = bodyMapper.apply(matcher, expressionString);
            resolvedScript.replace(matcher.start(), matcher.end(), result);
        }

        return resolvedScript.toString();
    }
}
