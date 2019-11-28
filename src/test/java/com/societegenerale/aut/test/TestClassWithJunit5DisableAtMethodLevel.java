package com.societegenerale.aut.test;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class TestClassWithJunit5DisableAtMethodLevel {

    @Test
    @Disabled
    public void someDisabledTestWithoutAComment() {

        assertThat(true).isTrue();

        fail("it failed ");

    }
}
