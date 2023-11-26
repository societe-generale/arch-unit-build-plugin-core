package com.societegenerale.commons.plugin.service;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaFileParserTest extends AbstractExcludePathTest
{
    private final JavaFileParser javaFileParser = new JavaFileParser();

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
    public void canParseJavaFile() throws IOException
    {
        final JavaFileParser.JavaFile fileWithPackage = javaFileParser.parse(getTempJavaFile(), getLogger());
        assertThat(fileWithPackage.getClassName()).isEqualTo(AbstractExcludePathTest.CLASS_NAME );
        assertThat(fileWithPackage.getPackageString()).isEqualTo(AbstractExcludePathTest.PACKAGE_NAME);
    }

    @Test
    public void canParseJavaFileWithoutPackage() throws IOException
    {
        final JavaFileParser.JavaFile fileWithoutPackage = javaFileParser.parse(getTempJavaFileWithDefaultPackage(), getLogger());
        assertThat(fileWithoutPackage.getClassName()).isEqualTo(AbstractExcludePathTest.CLASS_NAME);
        assertThat(fileWithoutPackage.getPackageString()).isNull();
    }

    @Test
    public void canParseJavaFileWithComments() throws IOException
    {
        final JavaFileParser.JavaFile fileWithFileComment = javaFileParser.parse(
                AbstractExcludePathTest.getTempJavaFileWithFileComment(), getLogger());
        assertThat(fileWithFileComment.getClassName()).isEqualTo(AbstractExcludePathTest.CLASS_NAME_WITH_FILE_COMMENT );
        assertThat(fileWithFileComment.getPackageString()).isEqualTo(AbstractExcludePathTest.PACKAGE_NAME);
    }


}
