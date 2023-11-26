package com.societegenerale.commons.plugin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.societegenerale.commons.plugin.service.ExcludedPathsPreProcessor.PACKAGE_INFO_JAVA;
import static org.assertj.core.api.Assertions.assertThat;

public class ExcludedPathsPreProcessorTest extends AbstractExcludePathTest
{
    private final ExcludedPathsPreProcessor preProcessor = new ExcludedPathsPreProcessor();

    @BeforeAll
    public static void init() throws IOException
    {
        AbstractExcludePathTest.init();
    }

    @AfterAll
    public static void cleanup() throws IOException
    {
        AbstractExcludePathTest.cleanup();
    }

    @Test
    public void testFindJavaFiles_EmptyDir() throws IOException
    {
        final Path emptyDir =
                Files.createDirectory(Paths.get(getTestTempRootDirectory().toString() + "/EmptyDirectory"));

        final Set<Path> actual = preProcessor.findJavaFiles(emptyDir, getLogger());

        assertThat(actual).isEmpty();
    }

    @Test
    public void testFindJavaFiles() throws IOException
    {
        final Path notAJavaFilePath =
                Files.createFile(Paths.get(getTestTempRootDirectory().toString() + "/NotAJavaFile.txt"));

        final Path packageInfoFilePath =
                Files.createFile(Paths.get(
                        getTestTempRootDirectory().toString() + "/" + PACKAGE_INFO_JAVA));

        final Set<Path> expected = new HashSet<>();
        expected.add(getTempJavaFile());
        expected.add(getTempJavaFileWithDefaultPackage());
        expected.add(AbstractExcludePathTest.getTempJavaFileWithFileComment());

        final Set<Path> actual = preProcessor.findJavaFiles(getTestTempRootDirectory(), getLogger());

        assertThat(actual).doesNotContain(notAJavaFilePath);
        assertThat(actual).doesNotContain(packageInfoFilePath);
        assertThat(actual).hasSameSizeAs(expected);
        assertThat(actual).containsOnlyElementsOf(expected);
    }

    @Test
    public void testDetermineClassNames_EmptySet()
    {
        final Set<String> actual = preProcessor.determineClassNames(new HashSet<>(), getLogger());
        assertThat(actual).isEmpty();
    }

    @Test
    public void testDetermineClassNames()
    {
        final Set<String> expected = new HashSet<>();
        expected.add(AbstractExcludePathTest.CLASS_NAME);
        expected.add(AbstractExcludePathTest.PACKAGE_NAME + "." + AbstractExcludePathTest.CLASS_NAME);
        expected.add(AbstractExcludePathTest.PACKAGE_NAME + "." + AbstractExcludePathTest.CLASS_NAME_WITH_FILE_COMMENT);

        final Set<Path> paths = new HashSet<>();
        paths.add(getTempJavaFile());
        paths.add(getTempJavaFileWithDefaultPackage());
        paths.add(AbstractExcludePathTest.getTempJavaFileWithFileComment());

        final Set<String> actual = preProcessor.determineClassNames(paths, getLogger());

        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    public void testConvertToPath() throws IOException
    {
        final Path targetDir = AbstractExcludePathTest.getTestProjectBuildDirectory();

        final Path generatedSourcesDir = Files.createDirectory(
                Paths.get(targetDir.toString(), ExcludedPathsPreProcessor.GENERATED_SOURCES));

        final Path actualGenSrc = preProcessor.convertToPath(getLogger(), targetDir.toString(),
                                                             ExcludedPathsPreProcessor.GENERATED_SOURCES);

        assertThat(actualGenSrc).isEqualTo(generatedSourcesDir);

        // Directory outside of the project
        final Path dirOutsideTarget =
                Files.createDirectory(Paths.get(getTestTempRootDirectory().toString(), "outsideDir"));

        final Path actualOutsideDir = preProcessor.convertToPath(getLogger(), targetDir.toString(),
                                                                 dirOutsideTarget.toString());
        assertThat(actualOutsideDir).isEqualTo(dirOutsideTarget);

        // path that point a to a file
        final Path javaFile = AbstractExcludePathTest.getTempJavaFileWithFileComment();
        final Path actualJavaFile = preProcessor.convertToPath(getLogger(), targetDir.toString(),
                                                               javaFile.toString());
        assertThat(actualJavaFile).isEqualTo(javaFile);
    }

    @Test
    public void testProcessExcludedPaths()
    {
        assertThat(preProcessor.processExcludedPaths(getLogger(), null, null)).isNotNull();

        final Path targetDir = AbstractExcludePathTest.getTestProjectBuildDirectory();

        //exclude all java files under directory com (...tempDir../target/com/...)
        final Set<String> excludes = preProcessor.processExcludedPaths(getLogger(), targetDir.toString(),
                                                                       Collections.singletonList("com"));

        assertThat(excludes).hasSize(3);

        assertThat(excludes).contains("com");
        assertThat(excludes).contains(AbstractExcludePathTest.PACKAGE_NAME + "." + AbstractExcludePathTest.CLASS_NAME);
        assertThat(excludes).contains(
                AbstractExcludePathTest.PACKAGE_NAME + "." + AbstractExcludePathTest.CLASS_NAME_WITH_FILE_COMMENT);
    }

    @Test
    public void testIsJavaFile()
    {
        assertThat(preProcessor.isJavaFile(null)).isFalse();
        assertThat(preProcessor.isJavaFile("")).isFalse();
        assertThat(preProcessor.isJavaFile("abc")).isFalse();

        assertThat(preProcessor.isJavaFile("c:\\" + PACKAGE_INFO_JAVA.toUpperCase())).isFalse();
        assertThat(preProcessor.isJavaFile("c:\\" + PACKAGE_INFO_JAVA.toLowerCase())).isFalse();
        assertThat(preProcessor.isJavaFile("c:\\" + PACKAGE_INFO_JAVA + "   ")).isFalse();
        assertThat(preProcessor.isJavaFile("c:\\" + PACKAGE_INFO_JAVA)).isFalse();

        final String pathString = "c:\\MyJavaFile.java";
        assertThat(preProcessor.isJavaFile(pathString.toUpperCase())).isTrue();
        assertThat(preProcessor.isJavaFile(pathString.toLowerCase())).isTrue();
        assertThat(preProcessor.isJavaFile(pathString + "   ")).isTrue();
        assertThat(preProcessor.isJavaFile(pathString)).isTrue();

    }
}
