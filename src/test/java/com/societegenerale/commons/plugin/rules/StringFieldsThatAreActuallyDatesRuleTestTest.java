package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class StringFieldsThatAreActuallyDatesRuleTestTest {

	private String pathClassWithStringsEndingWithTheWordDate = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithStringsEndingWithTheWordDate.class";

	private String pathClassWithoutStringsEndingWithTheWordDate = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithoutStringsEndingWithTheWordDate.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolationTest() {

		new StringFieldsThatAreActuallyDatesRuleTest().execute(pathClassWithStringsEndingWithTheWordDate,
				new DefaultScopePathProvider(), emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new StringFieldsThatAreActuallyDatesRuleTest()
				.execute(pathClassWithoutStringsEndingWithTheWordDate, new DefaultScopePathProvider(), emptySet()))
						.doesNotThrowAnyException();

	}

}
