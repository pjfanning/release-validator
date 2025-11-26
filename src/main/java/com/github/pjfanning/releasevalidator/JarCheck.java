package com.github.pjfanning.releasevalidator;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

class JarCheck {

    static String checkJar(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            Manifest man = jarFile.getManifest();
            Attributes attrs = man.getMainAttributes();
            System.out.println(attrs.get(Attributes.Name.MANIFEST_VERSION));
            Enumeration<JarEntry> entries = jarFile.entries();
            boolean moduleInfoClassFound = false;
            while (!moduleInfoClassFound && entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith("module-info.class")) {
                    moduleInfoClassFound = true;
                }
            }
            if (!moduleInfoClassFound) {
                return String.format("No module-info.class found in %s", file.getAbsolutePath());
            }
        } catch (Exception e) {
            return String.format("Failed to check jar %s: %s", file, e);
        }
        return null;
    }
}
