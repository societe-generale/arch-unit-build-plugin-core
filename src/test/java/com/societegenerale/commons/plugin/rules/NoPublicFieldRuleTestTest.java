package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

public class NoPublicFieldRuleTestTest {

	String pathObjectWithNoPublicField = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithNoNonStaticPublicField.class";

	String pathObjectWithPublicField = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithPublicField.class";

	@Before
	public void setup(){
		//in the normal lifecycle, ArchUtils is instantiated, which enables a static field there to be initialized
		ArchUtils archUtils=new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolations() {

		new NoPublicFieldRuleTest().execute(pathObjectWithPublicField, new DefaultScopePathProvider(),emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolation_even_with_publicStaticFinaField() {

		assertThatCode(() -> new NoPublicFieldRuleTest().execute(pathObjectWithNoPublicField, new DefaultScopePathProvider(),emptySet()))
				.doesNotThrowAnyException();

	}

}
