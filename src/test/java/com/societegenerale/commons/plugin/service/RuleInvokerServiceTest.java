package com.societegenerale.commons.plugin.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.model.ApplyOn;
import com.societegenerale.commons.plugin.model.ConfigurableRule;
import com.societegenerale.commons.plugin.model.Rules;
import com.societegenerale.commons.plugin.rules.HexagonalArchitectureTest;
import com.societegenerale.commons.plugin.rules.NoStandardStreamRuleTest;
import com.societegenerale.commons.plugin.rules.classesForTests.DummyCustomRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class RuleInvokerServiceTest {

    RuleInvokerService ruleInvokerService = new RuleInvokerService(new SilentLog(), new TestSpecificScopeProvider());

    ConfigurableRule configurableRule = new ConfigurableRule();

    private Log testLogger = new SilentLog();

    @Test
    public void shouldInvokePreConfiguredRulesMethod()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        Rules rules = new Rules(Arrays.asList(NoStandardStreamRuleTest.class.getName()), emptyList());

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("Rule 'no classes should access standard streams' was violated ");
    }

    @Test
    public void shouldInvokePreConfiguredRuleThatCanLog()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        Rules rules = new Rules(Arrays.asList(HexagonalArchitectureTest.class.getName()), emptyList());

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("Rule 'classes that reside in a package");
    }

    @Test
    public void shouldNotExecuteSkippedConfigurableRules()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.societegenerale.commons.plugin.rules", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(Arrays.asList("annotatedWithTest", "resideInMyPackage"));
        configurableRule.setSkip(true);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isEmpty();
    }

    @Test
    public void shouldExecuteConfigurableRuleWithNoPackageProvided_OnlyOnClassesOfScope()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn(null, "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(Arrays.asList("annotatedWithTest"));

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).doesNotContain("Class <com.societegenerale.aut.main.ObjectWithAdateField>");
        assertThat(errorMessage).contains("Class <com.societegenerale.aut.test.TestClassWithOutJunitAsserts>");
    }

    @Test
    public void shouldExecute2ConfigurableRulesOnTest()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.societegenerale.commons.plugin.rules", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(Arrays.asList("annotatedWithTest", "resideInMyPackage"));

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("classes should be annotated with @Test");
        assertThat(errorMessage).contains("classes should reside in a package 'myPackage'");
    }

    @Test
    public void shouldExecuteOnlyTheConfiguredRule()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.societegenerale.commons.plugin.rules", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(singletonList("annotatedWithTest"));

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("classes should be annotated with @Test");
        assertThat(errorMessage).doesNotContain("classes should reside in a package 'myPackage'");
    }

    @Test
    public void shouldExecuteAllRulesFromConfigurableClassByDefault()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.societegenerale.commons.plugin.rules", "main");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("classes should be annotated with @Test");
        assertThat(errorMessage).contains("classes should reside in a package 'myPackage'");
    }

    @Test
    public void shouldExecuteAllRulesOnSpecificPackageInTest()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.societegenerale.aut.test.specificCase", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("Rule 'classes should be annotated with @Test' was violated (1 times)");
        assertThat(errorMessage).contains("Rule 'classes should reside in a package 'myPackage'' was violated (1 times)");
    }

    @Test
    public void shouldExecuteAllRulesFromArchUnit_GeneralCodingRule()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.societegenerale.aut.test.specificCase", "test");

        configurableRule.setRule(GeneralCodingRules.class.getName());
        configurableRule.setApplyOn(applyOn);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains(
                "Rule 'no classes should use JodaTime, because modern Java projects use the [java.time] API instead' was violated (1 times)");
        assertThat(errorMessage).contains(
                "Field <com.societegenerale.aut.test.specificCase.DummyClassToValidate.anyJodaTimeObject> has type <org.joda.time.JodaTimePermission");
    }
}
