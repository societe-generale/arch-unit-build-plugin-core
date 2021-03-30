package com.societegenerale.commons.plugin.utils;

import java.net.URI;

import javax.annotation.Nonnull;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

public class ExclusionImportOption implements ImportOption {

    private static final String CLASS = ".class";

    private URI patternToExcludeDirectoryBased;

    public ExclusionImportOption(@Nonnull final String patternToExclude) {
        // assuming a directory based exclude path
        this.patternToExcludeDirectoryBased = toUri(patternToExclude);
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

        if(excludePath.endsWith(CLASS)) {
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
