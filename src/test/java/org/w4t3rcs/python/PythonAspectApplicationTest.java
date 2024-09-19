package org.w4t3rcs.python;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w4t3rcs.python.config.EnablePy4J;

@EnablePy4J
@SpringBootTest
class PythonAspectApplicationTest {
    private final TestPythonScript testPythonScript;

    @Autowired
    public PythonAspectApplicationTest(TestPythonScript testPythonScript) {
        this.testPythonScript = testPythonScript;
    }

    @Test
    void testDefaultPythonExecution() {
        testPythonScript.testPythonFileAfterMethod();
    }

    @Test
    void testPy4JExecution() {
        testPythonScript.testPy4JScriptAfterMethod();
    }
}
