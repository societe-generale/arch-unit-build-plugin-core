package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

public class TestMethodsNamingRuleTestTest {

	private String pathMethodWithIncorrectNameTest = "com/societegenerale/aut/test/MethodWithIncorrectNameTest.class";

	private String pathMethodWithCorrectNameStartingWithShouldTest = "com/societegenerale/aut/test/MethodWithCorrectNameStartingWithShouldTest.class";

	private String pathMethodWithCorrectNameStartingWithTestTest = "com/societegenerale/aut/test/MethodWithCorrectNameStartingWithTestTest.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation() {

		new TestMethodsNamingRuleTest().execute(pathMethodWithIncorrectNameTest, new TestSpecificScopeProvider(),
				emptySet());

	}

	@Test
	public void testShouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new TestMethodsNamingRuleTest().execute(pathMethodWithCorrectNameStartingWithShouldTest,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestMethodsNamingRuleTest().execute(pathMethodWithCorrectNameStartingWithTestTest,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

	}

}
