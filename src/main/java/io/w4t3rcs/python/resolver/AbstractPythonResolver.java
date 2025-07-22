package io.w4t3rcs.python.resolver;

import io.w4t3rcs.python.file.PythonFileHandler;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class defining the contract for resolving Python scripts.
 * Implementations of this interface process Python scripts before execution,
 * applying transformations or resolving expressions within the script.
 */
public abstract class AbstractPythonResolver implements PythonResolver {
    /**
     * Constant for a Python JSON import statement.
     * This string is automatically added to Python scripts that need JSON functionality.
     */
    protected static final String IMPORT_JSON = "import json\n";
    @Getter(AccessLevel.PROTECTED)
    private final PythonFileHandler pythonFileHandler;

    protected AbstractPythonResolver(PythonFileHandler pythonFileHandler) {
        this.pythonFileHandler = pythonFileHandler;
    }

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        String result = script;
        if (this.getPythonFileHandler().isPythonFile(script)) {
            result = this.getPythonFileHandler().readScriptBodyFromFile(script);
        }
        return handleResolve(result, arguments);
    }

    /**
     * Processes a Python script, applying transformations or resolving expressions.
     * It is used in {@link AbstractPythonResolver}.resolve(...) method as main behavior descriptor
     *
     * @param script The Python script content or file path to process
     * @param arguments A map of variables that may be used during resolution
     * @return The processed script after applying transformations
     */
    protected abstract String handleResolve(String script, Map<String, Object> arguments);

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
    protected String replaceScriptFragments(String script, String regex, int positionFromStart, int positionFromEnd, BiFunction<Matcher, String, String> bodyMapper) {
        StringBuilder resolvedScript = new StringBuilder(script);
        if (!script.contains(IMPORT_JSON)) resolvedScript.insert(0, IMPORT_JSON);
        Pattern spelPattern = Pattern.compile(regex);
        Matcher matcher = spelPattern.matcher(resolvedScript);
        while (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(positionFromStart, group.length() - positionFromEnd);
            String result = bodyMapper.apply(matcher, expressionString);
            resolvedScript.replace(matcher.start(), matcher.end(), result);
        }

        return resolvedScript.toString();
    }
}
