package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class FinalNonStaticFieldsHaveToBeStaticFinalFieldsRuleTestTest {

	private String pathClassWithFinalNonStaticFields = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithFinalNonStaticFields.class";

	private String pathClassWithStaticFinalFields = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithStaticFinalFields.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation() {

		new FinalNonStaticFieldsHaveToBeStaticFinalFieldsRuleTest().execute(pathClassWithFinalNonStaticFields,
				new DefaultScopePathProvider(), emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolation() {

		assertThatCode(() -> new FinalNonStaticFieldsHaveToBeStaticFinalFieldsRuleTest()
				.execute(pathClassWithStaticFinalFields, new DefaultScopePathProvider(), emptySet()))
						.doesNotThrowAnyException();

	}

}
