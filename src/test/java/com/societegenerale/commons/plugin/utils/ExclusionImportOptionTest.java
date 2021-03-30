package com.societegenerale.commons.plugin.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.tngtech.archunit.core.importer.Location;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExclusionImportOptionTest {

    private static Path testRootDir;

    private static Location locationToBeExcluded;
    private static Location locationToBeIncluded;

    @BeforeClass
    public static void init() throws IOException {
        testRootDir = Files.createTempDirectory("ExclusionImportOptionTest");
        locationToBeExcluded = Location.of(Paths.get(testRootDir.toString(), "com", "societegenerale", "commons", "plugin", "utils", "Foo.class"));
        locationToBeIncluded = Location.of(Paths.get(testRootDir.toString(), "com", "societegenerale", "commons", "plugin", "Bar.class"));
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        if (testRootDir != null) {
            Files.deleteIfExists(testRootDir);
        }
    }

    @Test
    public void includes_LocationNull() {
        ExclusionImportOption exclusionImportOptionPackage = new ExclusionImportOption("somePattern");
        assertFalse(exclusionImportOptionPackage.includes(null));
    }

    @Test
    public void includes_DirectoryBased() {

        String excludePathDirectorySlashes = "com/societegenerale/commons/plugin/utils";
        String excludePathDirectoryBackslashes = "com\\societegenerale\\commons\\plugin\\utils";

        ExclusionImportOption exclusionByDirectoryPatternSlashes = new ExclusionImportOption(
                excludePathDirectorySlashes);
        assertFalse(exclusionByDirectoryPatternSlashes.includes(locationToBeExcluded));
        assertTrue(exclusionByDirectoryPatternSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByDirectoryPatternBackslashes = new ExclusionImportOption(
                excludePathDirectoryBackslashes);
        assertFalse(exclusionByDirectoryPatternBackslashes.includes(locationToBeExcluded));
        assertTrue(exclusionByDirectoryPatternBackslashes.includes(locationToBeIncluded));
    }

    @Test
    public void includes_PackageBased() {

        String excludePathPackage = "com.societegenerale.commons.plugin.utils";

        ExclusionImportOption exclusionByPackage = new ExclusionImportOption(excludePathPackage);
        assertFalse(exclusionByPackage.includes(locationToBeExcluded));
        assertTrue(exclusionByPackage.includes(locationToBeIncluded));
    }

    @Test
    public void includes_ClassfileBased() {

        String excludePathClassfileWithSlashes = "com/societegenerale/commons/plugin/utils/Foo";
        String excludePathClassfileWithBackslashes = "com\\societegenerale\\commons\\plugin\\utils\\Foo";

        String excludePathClassfileWithTypeAndSlashes = "com/societegenerale/commons/plugin/utils/Foo.class";
        String excludePathClassfileWithTypeAndBackslashes = "com\\societegenerale\\commons\\plugin\\utils\\Foo.class";

        ExclusionImportOption exclusionByClassfileWithSlashes = new ExclusionImportOption(
                excludePathClassfileWithSlashes);
        assertFalse(exclusionByClassfileWithSlashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithTypeAndSlashes = new ExclusionImportOption(
                excludePathClassfileWithTypeAndSlashes);
        assertFalse(exclusionByClassfileWithTypeAndSlashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithTypeAndSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithBackslashes = new ExclusionImportOption(
                excludePathClassfileWithBackslashes);
        assertFalse(exclusionByClassfileWithBackslashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithBackslashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithTypeAndBackslashes = new ExclusionImportOption(
                excludePathClassfileWithTypeAndBackslashes);
        assertFalse(exclusionByClassfileWithTypeAndBackslashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithTypeAndBackslashes.includes(locationToBeIncluded));
    }

    @Test
    public void includes_ClasspathBased() {

        String excludePathClassfileWithPackage = "com.societegenerale.commons.plugin.utils.Foo";
        String excludePathClassfileWithTypeAndPackage = "com.societegenerale.commons.plugin.utils.Foo.class";

        ExclusionImportOption exclusionByPackage = new ExclusionImportOption(excludePathClassfileWithPackage);
        assertFalse(exclusionByPackage.includes(locationToBeExcluded));
        assertTrue(exclusionByPackage.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByTypeAndPackage = new ExclusionImportOption(excludePathClassfileWithTypeAndPackage);
        assertFalse(exclusionByTypeAndPackage.includes(locationToBeExcluded));
        assertTrue(exclusionByTypeAndPackage.includes(locationToBeIncluded));
    }
}
