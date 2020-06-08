package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Collection;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

@RunWith(Parameterized.class)
public class NoJavaUtilDateRuleTestTest {

	// instance variable for parameterized tests

	private String path;

	// pathClassThrowingException

	private String pathObjectWithAdateField = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithAdateField.class";

	/*
	 * pathsOfClassesNotThrowingAnyException They are static because they are used
	 * in static context
	 */

	private static String pathObjectWithJava8TimeLib = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithJava8TimeLib.class";

	private static String pathObjectWithJavaTextDateFormat = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithJavaTextDateFormat.class";

	private static String pathObjectWithJavaUtilGregorianCalendar = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithJavaUtilGregorianCalendar.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	public NoJavaUtilDateRuleTestTest(String path) {
		this.path = path;
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolationsTest() {

		new NoJavaUtilDateRuleTest().execute(pathObjectWithAdateField, new DefaultScopePathProvider(), emptySet());

	}

	@Parameterized.Parameters
	public static Collection pathsOfClassesNotThrowingAnyException() {
		return Arrays.asList(new String[] { pathObjectWithJava8TimeLib, pathObjectWithJavaTextDateFormat,
				pathObjectWithJavaUtilGregorianCalendar });
	}

	@Test
	public void shouldNotThrowAnyViolation() {

		assertThatCode(() -> new NoJavaUtilDateRuleTest().execute(path, new DefaultScopePathProvider(), emptySet()))
				.doesNotThrowAnyException();

	}

}
