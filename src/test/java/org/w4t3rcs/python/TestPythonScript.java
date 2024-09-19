package org.w4t3rcs.python;

import org.springframework.stereotype.Component;
import org.w4t3rcs.python.metadata.Py4JAfterMethod;
import org.w4t3rcs.python.metadata.PythonAfterMethod;
import org.w4t3rcs.python.metadata.SpelythonAfterMethod;

@Component
public class TestPythonScript {
    @PythonAfterMethod("test.py")
    public void testPythonAfterMethod() {
        System.out.println("hello from java");
    }

    @Py4JAfterMethod("test_py4j.py")
    public void testPy4JAfterMethod() {
        System.out.println("hello from java");
    }

    @SpelythonAfterMethod("test_spelython.py")
    public void testSpelythonAfterMethod() {
        System.out.println("hello from java");
    }
}
