package com.github.pjfanning.releasevalidator;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNull;

class PomTest {
    @Test
    void testPom() throws Exception{
        try (InputStream stream = PomTest.class.getResourceAsStream("/poi-5.5.1.pom")) {
            String result = PomCheck.checkPom(stream);
            assertNull(result);
        }
    }
}
