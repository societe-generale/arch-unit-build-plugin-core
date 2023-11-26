package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestClassesNamingRuleTestTest {

	private String pathTestClassWithIncorrectName1 = "com/societegenerale/aut/test/ClassTestWithIncorrectName1.class";

	private String pathTestClassWithIncorrectName2 = "com/societegenerale/aut/test/ClassTestWithIncorrectName2.class";

	private String pathClassWithCorrectName1Test = "com/societegenerale/aut/test/ClassTestWithCorrectName1Test.class";

	private String pathClassWithCorrectName2Test = "com/societegenerale/aut/test/ClassTestWithCorrectName2Test.class";

	@BeforeEach
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolation1Test() {
		assertThatThrownBy(() -> {
			new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName1, new TestSpecificScopeProvider(),
					emptySet());
		}).isInstanceOf(AssertionError.class);
	}

	@Test
	public void shouldThrowViolation2Test() {
		assertThatThrownBy(() -> {
			new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName2, new TestSpecificScopeProvider(),
					emptySet());
		}).isInstanceOf(AssertionError.class);

	}

	@Test
	public void shouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName1Test,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName2Test,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

}
