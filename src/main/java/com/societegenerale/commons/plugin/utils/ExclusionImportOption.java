package com.societegenerale.commons.plugin.utils;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

public class ExclusionImportOption implements ImportOption {

    String patternToExclude;

    public ExclusionImportOption(String patternToExclude) {
        this.patternToExclude=patternToExclude;
    }

    @Override
    public boolean includes(Location location) {

        if(location.contains(patternToExclude)){
            return false;
        }
        return true;
    }
}
