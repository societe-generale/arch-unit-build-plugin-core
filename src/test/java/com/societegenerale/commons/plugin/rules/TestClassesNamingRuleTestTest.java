package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class TestClassesNamingRuleTestTest {

	private String pathTestClassWithIncorrectName1 = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassTestWithIncorrectName1.class";

	private String pathTestClassWithIncorrectName2 = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassTestWithIncorrectName2.class";

	private String pathClassWithCorrectName1Test = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithCorrectName1Test.class";

	private String pathClassWithCorrectName2Test = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithCorrectName2Test.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation1Test() {

		new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName1, new DefaultScopePathProvider(),
				emptySet());

	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation2Test() {

		new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName2, new DefaultScopePathProvider(),
				emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName1Test,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName2Test,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

	}

}
