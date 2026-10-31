package com.societegenerale.commons.plugin.service;

import com.societegenerale.commons.plugin.SilentLog;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

interface TestInterfaceWithFieldOfTypeArchRule {
    ArchRule R = Mockito.mock(ArchRule.class);
}

public class InvokableRulesTest {

    @Test
    void testInvokeOnWithInterfaceContainingArchRuleField() {
        final JavaClasses javaClasses = new ClassFileImporter()
                .importClasses(TestInterfaceWithFieldOfTypeArchRule.class);
        var result = InvokableRules.of(
                TestInterfaceWithFieldOfTypeArchRule.class.getCanonicalName(),
                Collections.emptyList(),
                new SilentLog()
        ).invokeOn(javaClasses);
        Mockito.verify(TestInterfaceWithFieldOfTypeArchRule.R).check(Mockito.eq(javaClasses));
        assertThat(result.getMessage()).isEqualTo("");
    }
}
