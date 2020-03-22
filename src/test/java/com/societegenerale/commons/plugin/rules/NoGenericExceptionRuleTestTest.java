package com.societegenerale.commons.plugin.rules;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class NoGenericExceptionRuleTestTest {

	String pathObjectThrowingCustomException = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectThrowingCustomException.class";

	String pathObjectThrowingGenericException = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectThrowingGenericException.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolations() {

		new NoGenericExceptionRuleTest().execute(pathObjectThrowingGenericException, new DefaultScopePathProvider());

	}

	@Test
	public void shouldNotThrowAnyViolation() {

		assertThatCode(() -> new NoGenericExceptionRuleTest().execute(pathObjectThrowingCustomException,
				new DefaultScopePathProvider())).doesNotThrowAnyException();

	}

}
