package com.societegenerale.commons.plugin.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {

    @Test
    void shouldLoadRuleClassEvenIfConstructorIsPrivate() throws Exception {

        assertThat(ReflectionUtils.newInstance(Class.forName("com.tngtech.archunit.library.GeneralCodingRules"))).isNotNull();
    }

}
