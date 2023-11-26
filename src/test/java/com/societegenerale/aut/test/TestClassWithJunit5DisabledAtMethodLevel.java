package com.societegenerale.aut.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class TestClassWithJunit5DisabledAtMethodLevel {

    @Test
    @Disabled
    public void someDisabledTestWithoutAComment() {

        assertThat(true).isTrue();

        fail("it failed ");

    }
}
