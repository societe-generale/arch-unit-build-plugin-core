package com.societegenerale.commons.plugin.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionUtilsTest {

    @Test
    public void shouldLoadRuleClassEvenIfConstructorIsPrivate() throws ClassNotFoundException {

        assertThat(ReflectionUtils.newInstance(Class.forName("com.tngtech.archunit.library.GeneralCodingRules"))).isNotNull();
    }

}