package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

public class NoPublicFieldRuleTestTest {

	String pathObjectWithNoPublicField = "com/societegenerale/aut/main/ObjectWithNoNonStaticPublicField.class";

	String pathObjectWithPublicField = "com/societegenerale/aut/main/ObjectWithPublicField.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolations() {

		new NoPublicFieldRuleTest().execute(pathObjectWithPublicField, new TestSpecificScopeProvider(), emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolationEvenWithPublicStaticFinaField() {

		assertThatCode(() -> new NoPublicFieldRuleTest().execute(pathObjectWithNoPublicField,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

}
