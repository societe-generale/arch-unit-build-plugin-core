package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.NoPowerMockRuleTest.POWER_MOCK_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.aut.test.TestClassWithPowerMock;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class NoPowerMockRuleTestTest {

	// testClassWithoutPowerMock
	private String pathTestClassWithOutJunitAsserts = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithOutJunitAsserts.class";

	private String pathTestClassWithPowerMock = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithPowerMock.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoPowerMockRuleTest().execute(pathTestClassWithOutJunitAsserts,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoPowerMockRuleTest().execute(pathTestClassWithPowerMock, new DefaultScopePathProvider(), emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(TestClassWithPowerMock.class.getName())
				.hasMessageContaining(POWER_MOCK_VIOLATION_MESSAGE);

	}

}