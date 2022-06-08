package com.societegenerale.commons.plugin.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.SilentLogWithMemory;
import com.tngtech.archunit.core.importer.Location;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExclusionImportOptionTest {

    private static Path testRootDir;

    private static Location locationToBeExcluded;
    private static Location locationToBeIncluded;

    private final Log log = new SilentLogWithMemory();

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
        ExclusionImportOption exclusionImportOptionPackage = new ExclusionImportOption(log, "somePattern");
        assertFalse(exclusionImportOptionPackage.includes(null));
    }

    @Test
    public void includes_DirectoryBased() {

        String excludePathDirectorySlashes = "com/societegenerale/commons/plugin/utils";
        String excludePathDirectoryBackslashes = "com\\societegenerale\\commons\\plugin\\utils";

        ExclusionImportOption exclusionByDirectoryPatternSlashes = new ExclusionImportOption(
            log, excludePathDirectorySlashes);
        assertFalse(exclusionByDirectoryPatternSlashes.includes(locationToBeExcluded));
        assertTrue(exclusionByDirectoryPatternSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByDirectoryPatternBackslashes = new ExclusionImportOption(
            log, excludePathDirectoryBackslashes);
        assertFalse(exclusionByDirectoryPatternBackslashes.includes(locationToBeExcluded));
        assertTrue(exclusionByDirectoryPatternBackslashes.includes(locationToBeIncluded));
    }

    @Test
    public void includes_PackageBased() {

        String excludePathPackage = "com.societegenerale.commons.plugin.utils";

        ExclusionImportOption exclusionByPackage = new ExclusionImportOption(log, excludePathPackage);
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
            log, excludePathClassfileWithSlashes);
        assertFalse(exclusionByClassfileWithSlashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithTypeAndSlashes = new ExclusionImportOption(
            log, excludePathClassfileWithTypeAndSlashes);
        assertFalse(exclusionByClassfileWithTypeAndSlashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithTypeAndSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithBackslashes = new ExclusionImportOption(
            log, excludePathClassfileWithBackslashes);
        assertFalse(exclusionByClassfileWithBackslashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithBackslashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithTypeAndBackslashes = new ExclusionImportOption(
            log, excludePathClassfileWithTypeAndBackslashes);
        assertFalse(exclusionByClassfileWithTypeAndBackslashes.includes(locationToBeExcluded));
        assertTrue(exclusionByClassfileWithTypeAndBackslashes.includes(locationToBeIncluded));
    }

    @Test
    public void includes_ClasspathBased() {

        String excludePathClassfileWithPackage = "com.societegenerale.commons.plugin.utils.Foo";
        String excludePathClassfileWithTypeAndPackage = "com.societegenerale.commons.plugin.utils.Foo.class";

        ExclusionImportOption exclusionByPackage = new ExclusionImportOption(log, excludePathClassfileWithPackage);
        assertFalse(exclusionByPackage.includes(locationToBeExcluded));
        assertTrue(exclusionByPackage.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByTypeAndPackage = new ExclusionImportOption(log, excludePathClassfileWithTypeAndPackage);
        assertFalse(exclusionByTypeAndPackage.includes(locationToBeExcluded));
        assertTrue(exclusionByTypeAndPackage.includes(locationToBeIncluded));
    }
}
