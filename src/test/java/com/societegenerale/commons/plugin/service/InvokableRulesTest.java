package com.societegenerale.commons.plugin.service;

import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.model.RootClassFolder;
import com.societegenerale.commons.plugin.rules.classesForTests.DoubleIncludedCustomRule;
import com.societegenerale.commons.plugin.rules.classesForTests.DummyCustomRule;
import com.societegenerale.commons.plugin.rules.classesForTests.IncludedCustomRule;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClasses;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class InvokableRulesTest {

    @BeforeAll
    @SuppressWarnings("InstantiationOfUtilityClass")
    static void instantiateArchUtils() {
        new ArchUtils(mock(Log.class));
    }

    @Test
    void shouldInvokeAllRulesDefinedAsFields() {
        assertThat(invokeAndGetMessage(DummyCustomRule.class))
                .contains("Rule 'classes should be annotated with @Test' was violated")
                .contains("Rule 'classes should reside in a package 'myPackage'' was violated");
    }

    @Test
    void shouldInvokeSpecificRuleDefinedAsField() {
        assertThat(invokeAndGetMessage(DummyCustomRule.class, "annotatedWithTest"))
                .contains("Rule 'classes should be annotated with @Test' was violated")
                .doesNotContain("Rule 'classes should reside in a package 'myPackage'' was violated");
    }

    @Test
    void shouldInvokeAllRulesIncludedViaField() {
        assertThat(invokeAndGetMessage(IncludedCustomRule.class))
                .contains("Rule 'classes should be annotated with @Test' was violated")
                .contains("Rule 'classes should reside in a package 'myPackage'' was violated");
    }

    @Test
    void shouldInvokeSpecificRuleIncludedViaField() {
        assertThat(invokeAndGetMessage(IncludedCustomRule.class, "annotatedWithTest"))
                .contains("Rule 'classes should be annotated with @Test' was violated")
                .doesNotContain("Rule 'classes should reside in a package 'myPackage'' was violated");
    }

    @Test
    void shouldInvokeAllRulesIncludedViaFieldThatItselfIncludes() {
        assertThat(invokeAndGetMessage(DoubleIncludedCustomRule.class))
                .contains("Rule 'classes should be annotated with @Test' was violated")
                .contains("Rule 'classes should reside in a package 'myPackage'' was violated");
    }

    @Test
    void shouldInvokeSpecificRuleIncludedViaFieldThatItselfIncludes() {
        assertThat(invokeAndGetMessage(DoubleIncludedCustomRule.class,"annotatedWithTest"))
                .contains("Rule 'classes should be annotated with @Test' was violated")
                .doesNotContain("Rule 'classes should reside in a package 'myPackage'' was violated");
    }


    private static String invokeAndGetMessage(Class<?> rulesClass, String... checks) {
        JavaClasses javaClasses = ArchUtils.importAllClassesInPackage(new RootClassFolder(""), "");
        InvokableRules invokableRules = InvokableRules.of(rulesClass.getName(), Arrays.asList(checks), mock(Log.class));
        InvokableRules.InvocationResult invocationResult = invokableRules.invokeOn(javaClasses);
        return invocationResult.getMessage();
    }
}
