package com.societegenerale.commons.plugin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.Nonnull;

import com.societegenerale.commons.plugin.Log;
import com.tngtech.archunit.thirdparty.com.google.common.annotations.VisibleForTesting;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaSource;

class JavaFileParser
{
    protected static final String REGEX_LINE_BREAKS   = "[\n\r]";
    private static final String PACKAGE             = "package";
    private static final String CLASS               = "class";
    private static final String BLOCK_COMMENT_START = "/**";
    private static final String BLOCK_COMMENT_END   = "*/";
    private static final String LINE_COMMENT_START  = "//";
    private static final String LINE_COMMENT_END    = "\n";

    /**
     * Parse the file and returns {@link JavaFile}
     *
     * @param javafilePath - not null
     * @param logger  - not null
     *
     * @return not null
     */
    JavaFile parse(final Path javafilePath, final Log logger) throws IOException
    {

        var parsedJavaFile=Roaster.parseUnit(Files.newInputStream(javafilePath));
        JavaSource myClass = parsedJavaFile.getGoverningType();


        return new JavaFile(myClass.getPackage(), myClass.getName());
    }


    /**
     * Reads the java file.
     *
     * @param javafilePath - not null
     *
     * @return the file content as String if there is a content, not null
     *
     * @throws IOException
     */
    @VisibleForTesting
    String readFile(@Nonnull final Path javafilePath) throws IOException
    {
        String fileContent=""; //StringBuilder builder = new StringBuilder();
        var lines= Files.readAllLines(javafilePath);

        for(var line : lines){
            fileContent=fileContent+line;
        }

        return fileContent;
    }


    /**
     * Class that holds relevant java file information
     */
    static class JavaFile
    {
        private final String packageString;
        private final String className;

        JavaFile(final String packageString, final String className)
        {
            this.packageString = packageString == null || packageString.isEmpty() ? null : packageString;
            this.className = className == null || className.isEmpty() ? null : className;
        }

        String getPackageString()
        {
            return packageString;
        }

        String getClassName()
        {
            return className;
        }
    }
}
