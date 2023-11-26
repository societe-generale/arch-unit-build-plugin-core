package com.societegenerale.aut.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TestClassWithJunit5DisabledAtMethodLevelWithComment {

    @Test
    @Disabled("here's a comment explaining why it's ignored")
    public void someTestUsingAssertJ() {

        assertThat(true).isTrue();

        fail("it failed ");

    }
}
