package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.NoAutowiredFieldTest.NO_AUTOWIRED_FIELD_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.aut.test.TestClassWithAutowiredField;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class NoAutowiredFieldTestTest {

	private String pathTestClassWithAutowiredField = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithAutowiredField.class";

	// injected fields should not trigger autowired violation - they have their own rule
	private String pathTestClassWithInjectedField = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithInjectedField.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoAutowiredFieldTest().execute(pathTestClassWithAutowiredField, new DefaultScopePathProvider(),
					emptySet());
		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(TestClassWithAutowiredField.class.getName())
				.hasMessageContaining(NO_AUTOWIRED_FIELD_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoAutowiredFieldTest().execute(pathTestClassWithInjectedField,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();
	}

}
