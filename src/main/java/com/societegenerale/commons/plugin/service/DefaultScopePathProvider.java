package com.societegenerale.commons.plugin.service;

/**
 * Default scope path provider, returning typical values for Maven
 */
public class DefaultScopePathProvider implements ScopePathProvider {
    @Override
    public String getMainClassesPath() {
        return "/classes";
    }

    @Override
    public String getTestClassesPath() {
        return "/test-classes";
    }
}
