package com.societegenerale.commons.plugin.service;

import com.tngtech.archunit.core.domain.JavaClasses;

class ForTestForTestRule extends AbstractForTestRule {
    @Override
    protected void verify(JavaClasses javaClasses) {
       throw new AssertionError("okVerify");
    }
}
