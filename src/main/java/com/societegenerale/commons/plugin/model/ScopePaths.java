package com.societegenerale.commons.plugin.model;

/**
 * Can be used in the Maven or Gradle plugins that use the core, to override the scope paths in the configuration
 */
public class ScopePaths {

    private final String main;

    private final String test;

    public ScopePaths(String main, String test) {
        this.main = main;
        this.test = test;
    }

    public String getMain() {
        return main;
    }

    public String getTest() {
        return test;
    }

}
