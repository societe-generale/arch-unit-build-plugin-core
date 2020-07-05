package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.NoInjectedFieldTest.NO_INJECTED_FIELD_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.aut.test.TestClassWithInjectedField;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class NoInjectedFieldTestTest {

	// autowired fields should not trigger injected violations here - there's a separate rule for it 
	private String pathTestClassWithAutowiredFields = "./target/aut-target/test-classes/com/societegenerale/aut/test/TestClassWithAutowiredField.class";

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

			new NoInjectedFieldTest().execute(pathTestClassWithInjectedField, new DefaultScopePathProvider(),
					emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(TestClassWithInjectedField.class.getName())
				.hasMessageContaining(NO_INJECTED_FIELD_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoInjectedFieldTest().execute(pathTestClassWithAutowiredFields,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();
	}

}
