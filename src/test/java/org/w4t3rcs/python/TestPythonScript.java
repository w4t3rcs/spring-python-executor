package org.w4t3rcs.python;

import org.springframework.stereotype.Component;
import org.w4t3rcs.python.metadata.Py4JAfterMethod;
import org.w4t3rcs.python.metadata.PythonAfterMethod;

@Component
public class TestPythonScript {
    @PythonAfterMethod("test.py")
    public void testPythonFileAfterMethod() {
        System.out.println("hello from java");
    }

    @Py4JAfterMethod("print(entry_point)")
    public void testPy4JScriptAfterMethod() {
        System.out.println("hello from java");
    }
}
