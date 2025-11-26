package com.github.pjfanning.releasevalidator;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

class JarCheck {

    static String checkJar(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            Manifest man = jarFile.getManifest();
            Attributes attrs = man.getMainAttributes();
            System.out.println(attrs.get(Attributes.Name.MANIFEST_VERSION));
        } catch (Exception e) {
            return String.format("Failed to check jar %s: %s", file, e);
        }
        return null;
    }
}
