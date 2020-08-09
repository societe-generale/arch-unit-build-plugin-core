package com.societegenerale.commons.plugin.service;

import com.societegenerale.commons.plugin.model.RootClassFolder;

public interface ScopePathProvider {

    RootClassFolder getMainClassesPath();

    RootClassFolder getTestClassesPath();
}
