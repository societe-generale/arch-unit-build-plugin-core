package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestMethodsNamingRuleTestTest {

	private String pathMethodWithIncorrectNameTest = "com/societegenerale/aut/test/MethodWithIncorrectNameTest.class";

	private String pathMethodWithCorrectNameStartingWithShouldTest = "com/societegenerale/aut/test/MethodWithCorrectNameStartingWithShouldTest.class";

	private String pathMethodWithCorrectNameStartingWithTestTest = "com/societegenerale/aut/test/MethodWithCorrectNameStartingWithTestTest.class";

	@BeforeEach
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolation() {
		assertThatThrownBy(() -> {
			new TestMethodsNamingRuleTest().execute(pathMethodWithIncorrectNameTest, new TestSpecificScopeProvider(),
					emptySet());
		}).isInstanceOf(AssertionError.class);
	}

	@Test
	public void testShouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new TestMethodsNamingRuleTest().execute(pathMethodWithCorrectNameStartingWithShouldTest,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestMethodsNamingRuleTest().execute(pathMethodWithCorrectNameStartingWithTestTest,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

}
