package org.w4t3rcs.python;

import org.w4t3rcs.python.metadata.Py4JComponent;

@Py4JComponent
public class TestPythonComponent {
    public String getHello() {
        return "hello from java";
    }
}
