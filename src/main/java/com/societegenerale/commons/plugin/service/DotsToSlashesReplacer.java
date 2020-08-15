package com.societegenerale.commons.plugin.service;

public class DotsToSlashesReplacer {

    private DotsToSlashesReplacer() {
    }

    /**
     * package names, usually provided as dot separated string, need to be converted in file path, which are slash separated.
     * This method will do the replacement, excluding the dots that may be there at the beginning of the string
     */
    static String replace(String input) {

        //TODO this is really hack-ish.. should be replaced by a regexp based solution
        // unit test is there to help with implementation

        if (input.startsWith("..")) {
            return ".." + input.substring(2).replace(".", "/");
        } else if (input.startsWith(".")) {
            return "." + input.substring(1).replace(".", "/");
        } else {
            return input.replace(".", "/");
        }
    }
}
