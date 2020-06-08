package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

@RunWith(Parameterized.class)
public class TestMethodsNamingRuleTestTest {

	// instance variable for parameterized tests

	private String path;

	private String pathMethodWithIncorrectNameTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/MethodWithIncorrectNameTest.class";

	private static String pathMethodWithCorrectNameStartingWithShouldTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/MethodWithCorrectNameStartingWithShouldTest.class";

	private static String pathMethodWithCorrectNameStartingWithTestTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/MethodWithCorrectNameStartingWithTestTest.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	public TestMethodsNamingRuleTestTest(String path) {
		this.path = path;
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation() {

		new TestMethodsNamingRuleTest().execute(pathMethodWithIncorrectNameTest, new DefaultScopePathProvider(),
				emptySet());

	}

	@Parameterized.Parameters
	public static Collection pathsOfClassesNotThrowingAnyException() {
		return Arrays.asList(new String[] { pathMethodWithCorrectNameStartingWithShouldTest,
				pathMethodWithCorrectNameStartingWithTestTest });
	}

	@Test
	public void testShouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new TestMethodsNamingRuleTest().execute(path, new DefaultScopePathProvider(), emptySet()))
				.doesNotThrowAnyException();

	}

}
