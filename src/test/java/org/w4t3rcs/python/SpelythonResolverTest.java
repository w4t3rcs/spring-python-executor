package org.w4t3rcs.python;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w4t3rcs.python.service.PythonCompletionResolver;

@SpringBootTest
public class SpelythonResolverTest {
    private final PythonCompletionResolver spelythonResolver;

    @Autowired
    public SpelythonResolverTest(PythonCompletionResolver spelythonResolver) {
        this.spelythonResolver = spelythonResolver;
    }

    @Test
    void testSprythonResolver() {
        String resolved = spelythonResolver.resolve("print('${2 + 4}')");
        System.out.println(resolved);
    }
}
