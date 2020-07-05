package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.NoJunitAssertRuleTest.NO_JUNIT_ASSERT_DESCRIPTION;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.aut.test.TestClassWithJunit4Asserts;
import com.societegenerale.aut.test.TestClassWithJunit5Asserts;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class NoJunitAssertRuleTestTest {

	private String pathTestClassWithJunit4Asserts = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithJunit4Asserts.class";

	private String pathTestClassWithJunit5Asserts = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithJunit5Asserts.class";

	private String pathTestClassWithOutJunitAsserts = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithOutJunitAsserts.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoJunitAssertRuleTest().execute(pathTestClassWithOutJunitAsserts,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();
	}

	@Test
	public void shouldThrowForJunit4Violations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoJunitAssertRuleTest().execute(pathTestClassWithJunit4Asserts, new DefaultScopePathProvider(),
					emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageContaining("was violated (2 times)")
				.hasMessageContaining("calls method <org.junit.Assert.assertTrue(boolean)")
				.hasMessageContaining("calls method <org.junit.Assert.fail(java.lang.String)");

		assertThat(validationExceptionThrown).hasMessageStartingWith("Architecture Violation")
				.hasMessageContaining(TestClassWithJunit4Asserts.class.getName())
				.hasMessageContaining(NO_JUNIT_ASSERT_DESCRIPTION);
	}

	@Test
	public void shouldThrowForJunit5Violations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoJunitAssertRuleTest().execute(pathTestClassWithJunit5Asserts, new DefaultScopePathProvider(),
					emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageContaining("was violated (2 times)")
				.hasMessageContaining(
						"calls method <org.junit.jupiter.api.Assertions.assertEquals(java.lang.Object, java.lang.Object)")
				.hasMessageContaining("org.junit.jupiter.api.Assertions.fail(java.lang.String)");

		assertThat(validationExceptionThrown).hasMessageStartingWith("Architecture Violation")
				.hasMessageContaining(TestClassWithJunit5Asserts.class.getName())
				.hasMessageContaining(NO_JUNIT_ASSERT_DESCRIPTION);
	}

}