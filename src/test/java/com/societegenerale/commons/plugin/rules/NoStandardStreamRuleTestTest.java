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
public class NoStandardStreamRuleTestTest {

	private String path;

	private static String pathTestClassWithoutStandardStream1 = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithoutStandardStream1.class";

	private static String pathTestClassWithoutStandardStream2 = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithoutStandardStream2.class";

	private String pathTestClassWithStandardStream1 = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithStandardStream1.class";

	private String pathTestClassWithStandardStream2 = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithStandardStream2.class";

	public NoStandardStreamRuleTestTest(String path) {
		this.path = path;
	}

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Parameterized.Parameters
	public static Collection pathsOfClassesNotThrowingAnyException() {
		return Arrays.asList(new String[] { pathTestClassWithoutStandardStream1, pathTestClassWithoutStandardStream2 });
	}

	@Test
	public void shouldNotThrowViolations() {

		assertThatCode(() -> new NoStandardStreamRuleTest().execute(path, new DefaultScopePathProvider(), emptySet()))
				.doesNotThrowAnyException();

	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolations1() {

		new NoStandardStreamRuleTest().execute(pathTestClassWithStandardStream1, new DefaultScopePathProvider(),
				emptySet());

	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolations2() {

		new NoStandardStreamRuleTest().execute(pathTestClassWithStandardStream2, new DefaultScopePathProvider(),
				emptySet());

	}

}
