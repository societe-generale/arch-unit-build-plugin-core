package com.societegenerale.commons.plugin.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {

    @Test
    void shouldLoadRuleClassEvenIfConstructorIsPrivate() throws Exception {

        assertThat(ReflectionUtils.newInstance(Class.forName("com.tngtech.archunit.library.GeneralCodingRules"))).isNotNull();
    }

    @Test
    void testGetAllFieldsWithNoneElements() {
        interface X {
        }
        assertThat(ReflectionUtils.getAllFields(X.class).size()).isEqualTo(0);
    }

    interface Y {
        int A = 0;
    }

    @Test
    void testGetAllFieldsWithOneField() {
        assertThat(ReflectionUtils.getAllFields(Y.class).getFirst().getName()).isEqualTo("A");
    }

    @Test
    void testGetAllFieldsWithParentInterface() {
        interface Z extends Y {
        }
        assertThat(ReflectionUtils.getAllFields(Z.class).getFirst().getName()).isEqualTo("A");
    }
}
