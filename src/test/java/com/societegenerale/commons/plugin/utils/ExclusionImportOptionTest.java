package com.societegenerale.commons.plugin.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tngtech.archunit.core.importer.Location;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExclusionImportOptionTest {

    private static String excludePathDirectorySlashes     = "com/societegenerale/commons/plugin/utils";
    private static String excludePathDirectoryBackslashes = "com\\societegenerale\\commons\\plugin\\utils";
    private static String excludePathPackage              = "com.societegenerale.commons.plugin.utils";

    private static String excludePathClassfileWithSlashes     = "com/societegenerale/commons/plugin/utils/Foo";
    private static String excludePathClassfileWithBackslashes = "com\\societegenerale\\commons\\plugin\\utils\\Foo";
    private static String excludePathClassfileWithPackage     = "com.societegenerale.commons.plugin.utils.Foo";

    private static String excludePathClassfileWithTypeAndSlashes     = "com/societegenerale/commons/plugin/utils/Foo.class";
    private static String excludePathClassfileWithTypeAndBackslashes = "com\\societegenerale\\commons\\plugin\\utils\\Foo.class";
    private static String excludePathClassfileWithTypeAndPackage     = "com.societegenerale.commons.plugin.utils.Foo.class";

    private static Path testRootDir;

    private static Location locationToBeExclude;
    private static Location locationToBeIncluded;



    @BeforeClass
    public static void init() throws IOException {
       testRootDir = Files.createTempDirectory("ExclusionImportOptionTest");
        locationToBeExclude =  Location.of(Paths.get(testRootDir.toString(),"com", "societegenerale", "commons", "plugin", "utils", "Foo.class"));
        locationToBeIncluded =  Location.of(Paths.get(testRootDir.toString(),"com", "societegenerale", "commons", "plugin", "Bar.class"));
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        if(testRootDir != null){
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

        ExclusionImportOption exclusionByDirectoryPatternSlashes = new ExclusionImportOption(
                excludePathDirectorySlashes);
        assertFalse(exclusionByDirectoryPatternSlashes.includes(locationToBeExclude));
        assertTrue(exclusionByDirectoryPatternSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByDirectoryPatternBackslashes = new ExclusionImportOption(
                excludePathDirectoryBackslashes);
        assertFalse(exclusionByDirectoryPatternBackslashes.includes(locationToBeExclude));
        assertTrue(exclusionByDirectoryPatternBackslashes.includes(locationToBeIncluded));
    }

    @Test
    public void includes_PackageBased() {
        ExclusionImportOption exclusionByPackage = new ExclusionImportOption(excludePathPackage);
        assertFalse(exclusionByPackage.includes(locationToBeExclude));
        assertTrue(exclusionByPackage.includes(locationToBeIncluded));
    }

    @Test
    public void includes_ClassfileBased() {

        ExclusionImportOption exclusionByClassfileWithSlashes = new ExclusionImportOption(
                excludePathClassfileWithSlashes);
        assertFalse(exclusionByClassfileWithSlashes.includes(locationToBeExclude));
        assertTrue(exclusionByClassfileWithSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithTypeAndSlashes = new ExclusionImportOption(
                excludePathClassfileWithTypeAndSlashes);
        assertFalse(exclusionByClassfileWithTypeAndSlashes.includes(locationToBeExclude));
        assertTrue(exclusionByClassfileWithTypeAndSlashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithBackslashes = new ExclusionImportOption(
                excludePathClassfileWithBackslashes);
        assertFalse(exclusionByClassfileWithBackslashes.includes(locationToBeExclude));
        assertTrue(exclusionByClassfileWithBackslashes.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByClassfileWithTypeAndBackslashes = new ExclusionImportOption(
                excludePathClassfileWithTypeAndBackslashes);
        assertFalse(exclusionByClassfileWithTypeAndBackslashes.includes(locationToBeExclude));
        assertTrue(exclusionByClassfileWithTypeAndBackslashes.includes(locationToBeIncluded));
    }

    @Test
    public void includes_ClasspathBased() {
        ExclusionImportOption exclusionByPackage = new ExclusionImportOption(excludePathClassfileWithPackage);
        assertFalse(exclusionByPackage.includes(locationToBeExclude));
        assertTrue(exclusionByPackage.includes(locationToBeIncluded));

        ExclusionImportOption exclusionByTypeAndPackage = new ExclusionImportOption(excludePathClassfileWithTypeAndPackage);
        assertFalse(exclusionByTypeAndPackage.includes(locationToBeExclude));
        assertTrue(exclusionByTypeAndPackage.includes(locationToBeIncluded));
    }
}