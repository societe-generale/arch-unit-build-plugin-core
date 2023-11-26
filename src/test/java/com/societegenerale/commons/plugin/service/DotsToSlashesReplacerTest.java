package com.societegenerale.commons.plugin.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class DotsToSlashesReplacerTest {

    @Test
    public void shouldNotReplaceFirstDot(){

        String input=".blabla.hello.world";
        assertThat(DotsToSlashesReplacer.replace(input)).isEqualTo(".blabla/hello/world");

    }

    @Test
    public void shouldReplaceAllDotsExceptFirstOnes(){

        String input2="..blabla.hello.world";
        assertThat(DotsToSlashesReplacer.replace(input2)).isEqualTo("..blabla/hello/world");

    }

}
