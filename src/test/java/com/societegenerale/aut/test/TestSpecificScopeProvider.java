package com.societegenerale.aut.test;

import com.societegenerale.commons.plugin.service.ScopePathProvider;

public class TestSpecificScopeProvider implements ScopePathProvider {

    @Override
    public String getMainClassesPath() {
        return "./target/aut-target/classes/";
    }

    @Override
    public String getTestClassesPath() {
        return "./target/aut-target/test-classes/";
    }
}
