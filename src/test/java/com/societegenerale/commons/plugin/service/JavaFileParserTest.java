package com.societegenerale.commons.plugin.service;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaFileParserTest extends AbstractExcludePathTest
{
    private final JavaFileParser javaFileParser = new JavaFileParser();

    @BeforeAll
    static void init() throws IOException
    {
        AbstractExcludePathTest.init();
    }

    @AfterAll
    static void cleanup() throws IOException
    {
        AbstractExcludePathTest.cleanup();
    }

    @Test
    void canParseJavaFile() throws Exception
    {
        final JavaFileParser.JavaFile fileWithPackage = javaFileParser.parse(getTempJavaFile(), getLogger());
        assertThat(fileWithPackage.getClassName()).isEqualTo(AbstractExcludePathTest.CLASS_NAME );
        assertThat(fileWithPackage.getPackageString()).isEqualTo(AbstractExcludePathTest.PACKAGE_NAME);
    }

    @Test
    void canParseJavaFileWithoutPackage() throws Exception
    {
        final JavaFileParser.JavaFile fileWithoutPackage = javaFileParser.parse(getTempJavaFileWithDefaultPackage(), getLogger());
        assertThat(fileWithoutPackage.getClassName()).isEqualTo(AbstractExcludePathTest.CLASS_NAME);
        assertThat(fileWithoutPackage.getPackageString()).isNull();
    }

    @Test
    void canParseJavaFileWithComments() throws Exception
    {
        final JavaFileParser.JavaFile fileWithFileComment = javaFileParser.parse(
                AbstractExcludePathTest.getTempJavaFileWithFileComment(), getLogger());
        assertThat(fileWithFileComment.getClassName()).isEqualTo(AbstractExcludePathTest.CLASS_NAME_WITH_FILE_COMMENT );
        assertThat(fileWithFileComment.getPackageString()).isEqualTo(AbstractExcludePathTest.PACKAGE_NAME);
    }


}
