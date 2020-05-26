package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class TestMethodsNamingRuleTestTest {

	private String pathMethodWithIncorrectNameTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/MethodWithIncorrectNameTest.class";

	private String pathMethodWithCorrectNameStartingWithShouldTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/MethodWithCorrectNameStartingWithShouldTest.class";

	private String pathMethodWithCorrectNameStartingWithTestTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/MethodWithCorrectNameStartingWithTestTest.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation() {

		new TestMethodsNamingRuleTest().execute(pathMethodWithIncorrectNameTest, new DefaultScopePathProvider(),
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
