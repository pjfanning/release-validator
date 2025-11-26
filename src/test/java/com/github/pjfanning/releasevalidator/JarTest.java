package com.github.pjfanning.releasevalidator;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

class JarTest {
    // all tests assume that you have a file called /tmp/test.jar
    private final File testJar = new File("/tmp/test.jar");

    @Test
    void testJar() throws Exception{
        assertNull(JarCheck.checkJar(testJar));
    }
}
