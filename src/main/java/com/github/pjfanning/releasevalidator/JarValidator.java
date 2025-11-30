package com.github.pjfanning.releasevalidator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@Command(name = "JarValidator", mixinStandardHelpOptions = true, version = "0.1.0",
        description = "Validates Jars and related Pom files for a release.")
class JarValidator implements Callable<Integer> {
    @Parameters(index = "0", description = "The directory to check.")
    private File directory;

    @Override
    public Integer call() throws Exception {
        if  (directory == null) {
            throw new IllegalArgumentException("Directory is required");
        } else if  (!directory.isDirectory()) {
            throw new IllegalArgumentException("Provided directory is not a directory");
        }
        System.out.println("Validating " + directory);
        File[] pomFiles = directory.listFiles(f ->
            !f.isDirectory() && f.getName().endsWith(".pom"));
        ArrayList<String> issues = new ArrayList<>();
        if (pomFiles == null || pomFiles.length == 0) {
            System.err.println("WARN: No POM files found in directory " + directory);
        } else {
            for (File pomFile : pomFiles) {
                System.out.println("checking pom " + pomFile);
                String result = checkPomFile(pomFile);
                if (result != null) {
                    issues.add(result);
                }
            }
        }
        File[] jarFiles = directory.listFiles(f ->
                !f.isDirectory() && f.getName().endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            System.err.println("WARN: No Jar files found in directory " + directory);
        } else {
            for (File jarFile : jarFiles) {
                System.out.println("checking jar " + jarFile);
                String result = JarCheck.checkJar(jarFile);
                if (result != null) {
                    issues.add(result);
                }
            }
        }

        if (!issues.isEmpty()) {
            for  (String issue : issues) {
                System.err.println(issue);
            }
            return CommandLine.ExitCode.SOFTWARE;
        }
        return CommandLine.ExitCode.OK;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new JarValidator()).execute(args);
        System.exit(exitCode);
    }

    /**
     * @param pomFile
     * @return a String with an error message or null is nothing to report
     */
    private static String checkPomFile(File pomFile) {
        try (InputStream is = Files.newInputStream(pomFile.toPath())) {
            return PomCheck.checkPom(is);
        } catch (Exception e) {
            return String.format("Failed to parse %s: %s", pomFile, e);
        }
    }

}
