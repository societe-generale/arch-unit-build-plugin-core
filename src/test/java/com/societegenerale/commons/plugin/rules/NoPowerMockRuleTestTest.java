package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.test.TestClassWithPowerMock;
import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.societegenerale.commons.plugin.rules.NoPowerMockRuleTest.POWER_MOCK_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoPowerMockRuleTestTest {

	// testClassWithoutPowerMock
	private String pathTestClassWithOutJunitAsserts = "com/societegenerale/aut/test/TestClassWithOutJunitAsserts.class";

	private String pathTestClassWithPowerMock = "com/societegenerale/aut/test/TestClassWithPowerMock.class";

	@BeforeEach
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoPowerMockRuleTest().execute(pathTestClassWithOutJunitAsserts,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoPowerMockRuleTest().execute(pathTestClassWithPowerMock, new TestSpecificScopeProvider(), emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(TestClassWithPowerMock.class.getName())
				.hasMessageContaining(POWER_MOCK_VIOLATION_MESSAGE);

	}

}
