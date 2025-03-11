package com.societegenerale.commons.plugin.rules.classesForTests;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;

public class IncludedCustomRule {

    @ArchTest
    static final ArchTests INCLUDED = ArchTests.in(DummyCustomRule.class);
}
