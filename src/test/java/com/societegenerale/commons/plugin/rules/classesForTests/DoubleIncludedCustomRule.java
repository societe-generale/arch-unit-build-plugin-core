package com.societegenerale.commons.plugin.rules.classesForTests;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;

public class DoubleIncludedCustomRule {

    @ArchTest
    static final ArchTests DOUBLE_INCLUDED = ArchTests.in(IncludedCustomRule.class);
}
