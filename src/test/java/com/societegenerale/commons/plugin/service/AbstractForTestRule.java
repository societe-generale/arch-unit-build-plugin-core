package com.societegenerale.commons.plugin.service;

import com.tngtech.archunit.core.domain.JavaClasses;

abstract class AbstractForTestRule {
    public void execute(JavaClasses javaClasses) {
        this.verify(javaClasses);
    }

    protected abstract void verify(JavaClasses javaClasses);
}
