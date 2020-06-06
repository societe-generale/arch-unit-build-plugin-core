package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class TestClassesNamingRuleTestTest {

	// Incorrect Names

	private String pathTestClassWithIncorrectName1 = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassTestWithIncorrectName1.class";

	private String pathTestClassWithIncorrectName2 = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassTestWithIncorrectName2.class";

	private String pathTestClassWithIncorrectName3 = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassTestWithIncorrectName3.class";

	private String pathTestClassWithIncorrectName4 = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassTestWithIncorrectName4.class";

	// Correct Names

	private String pathClassWithCorrectName1Test = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithCorrectName1Test.class";

	private String pathClassWithCorrectName2Test = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithCorrectName2Test.class";

	private String pathClassWithCorrectName3Test = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithCorrectName3Test.class";

	private String pathClassWithCorrectName4Test = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithCorrectName4Test.class";

	// No Method Annotated With Test

	private String pathClass1WithNoMethodAnnotatedWithTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/Class1WithNoMethodAnnotatedWithTest.class";

	private String pathClass2WithNoMethodAnnotatedWithTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/Class2WithNoMethodAnnotatedWithTest.class";

	private String pathClass3WithNoMethodAnnotatedWithTest = "./target/aut-target/test-classes/com/societegenerale/aut/test/Class3WithNoMethodAnnotatedWithTest.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	// Incorrect Names

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

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation3Test() {

		new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName3, new DefaultScopePathProvider(),
				emptySet());

	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation4Test() {

		new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName4, new DefaultScopePathProvider(),
				emptySet());

	}

	// Correct Names & No Method Annotated With Test

	@Test
	public void shouldNotThrowAnyViolationTest() {

		// Correct Names

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName1Test,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName2Test,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName3Test,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName4Test,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		// No Method Annotated With Test

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClass1WithNoMethodAnnotatedWithTest,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClass2WithNoMethodAnnotatedWithTest,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClass3WithNoMethodAnnotatedWithTest,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

	}

}
