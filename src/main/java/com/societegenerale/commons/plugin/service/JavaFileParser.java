package com.societegenerale.commons.plugin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.societegenerale.commons.plugin.Log;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaSource;

class JavaFileParser
{

    /**
     * Parse the file and returns {@link JavaFile}
     *
     * @param javafilePath - not null
     * @param logger  - not null
     *
     * @return a JavaFile. package attribute may be null if the class is in default package
     */
    JavaFile parse(final Path javafilePath, final Log logger) throws IOException
    {

        var parsedJavaFile=Roaster.parseUnit(Files.newInputStream(javafilePath));
        JavaSource myClass = parsedJavaFile.getGoverningType();


        return new JavaFile(myClass.getPackage(), myClass.getName());
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
