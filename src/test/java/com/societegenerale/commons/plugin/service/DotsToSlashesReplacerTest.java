package com.societegenerale.commons.plugin.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DotsToSlashesReplacerTest {

    @Test
    void shouldNotReplaceFirstDot(){

        String input=".blabla.hello.world";
        assertThat(DotsToSlashesReplacer.replace(input)).isEqualTo(".blabla/hello/world");

    }

    @Test
    void shouldReplaceAllDotsExceptFirstOnes(){

        String input2="..blabla.hello.world";
        assertThat(DotsToSlashesReplacer.replace(input2)).isEqualTo("..blabla/hello/world");

    }

}
