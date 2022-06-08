package com.societegenerale.commons.plugin.utils;

import com.societegenerale.commons.plugin.Log;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import java.net.URI;
import javax.annotation.Nonnull;

public class ExclusionImportOption implements ImportOption {

    private static final String CLASS = ".class";

    private final URI patternToExcludeDirectoryBased;

    private final Log log;

    public ExclusionImportOption(Log log, @Nonnull final String patternToExclude) {

        this.log=log;
        // assuming a directory based exclude path
        log.debug("configuring a pattern to exclude : "+patternToExclude);
        this.patternToExcludeDirectoryBased = toUri(patternToExclude);
        log.debug("\t pattern converted into : "+patternToExcludeDirectoryBased.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includes(Location location) {
        return location != null && !location.contains(patternToExcludeDirectoryBased.toString());
    }

    private URI toUri(@Nonnull final String excludePath){
        boolean excludePathEndsWithDotClass = excludePath.endsWith(CLASS);
        String stringToConvert = excludePath;

        if(excludePathEndsWithDotClass) {
            // remove .class for easier replacements
            stringToConvert = excludePath.substring(0, excludePath.length() - CLASS.length());
        }

        // assuming a package based exclude path
        if(stringToConvert.contains(".")){
            stringToConvert = stringToConvert.replaceAll("\\.", "/");
        }
        // assuming a directory based exclude path
        else {
            // replace backslash by slash
            stringToConvert = stringToConvert.replaceAll("\\\\", "/");
        }

        if(excludePathEndsWithDotClass){
            // appends again .class that we removed initially
            stringToConvert = stringToConvert.concat(CLASS);
        }

        return URI.create(stringToConvert);
    }
}
