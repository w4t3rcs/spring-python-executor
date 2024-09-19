package org.w4t3rcs.python;

import org.springframework.stereotype.Component;
import org.w4t3rcs.python.metadata.Py4JAfterMethod;
import org.w4t3rcs.python.metadata.Py4JBeforeMethod;
import org.w4t3rcs.python.metadata.PythonAfterMethod;
import org.w4t3rcs.python.metadata.PythonBeforeMethod;

@Component
public class TestPythonScript {
    @PythonBeforeMethod("print('hello from python')")
    public void testPythonScriptBeforeMethod() {
        System.out.println("hello from java");
    }

    @PythonBeforeMethod("test.py")
    public void testPythonFileBeforeMethod() {
        System.out.println("hello from java");
    }

    @PythonAfterMethod("print('hello from python')")
    public void testPythonScriptAfterMethod() {
        System.out.println("hello from java");
    }

    @PythonAfterMethod("test.py")
    public void testPythonFileAfterMethod() {
        System.out.println("hello from java");
    }

    @Py4JBeforeMethod("print(entry_point)")
    public void testPy4JScriptBeforeMethod() {
        System.out.println("hello from java");
    }

    @Py4JBeforeMethod("test_py4j.py")
    public void testPy4JFileBeforeMethod() {
        System.out.println("hello from java");
    }

    @Py4JAfterMethod("print(entry_point)")
    public void testPy4JScriptAfterMethod() {
        System.out.println("hello from java");
    }

    @Py4JAfterMethod("test_py4j.py")
    public void testPy4JFileAfterMethod() {
        System.out.println("hello from java");
    }
}
