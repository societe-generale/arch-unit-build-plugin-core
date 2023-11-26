package com.societegenerale.aut.test;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestClassWithJunit4Asserts {

    @Test
    public void someTest() {

        assertTrue(true);

        fail("it failed ");

    }
}
