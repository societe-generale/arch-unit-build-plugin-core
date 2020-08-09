package com.societegenerale.aut.test;

import com.societegenerale.commons.plugin.model.RootClassFolder;
import com.societegenerale.commons.plugin.service.ScopePathProvider;

public class TestSpecificScopeProvider implements ScopePathProvider {

    @Override
    public RootClassFolder getMainClassesPath() {
        return new RootClassFolder("./target/aut-target/classes/");
    }

    @Override
    public RootClassFolder getTestClassesPath() {
        return new RootClassFolder("./target/aut-target/test-classes/");
    }
}
