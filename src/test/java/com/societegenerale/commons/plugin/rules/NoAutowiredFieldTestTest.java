package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.main.ClassWithAutowiredField;
import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.societegenerale.commons.plugin.rules.NoAutowiredFieldTest.NO_AUTOWIRED_FIELD_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoAutowiredFieldTestTest {

	private String pathTestClassWithAutowiredField = "com/societegenerale/aut/main/ClassWithAutowiredField.class";

	// injected fields should not trigger autowired violation - they have their own rule
	private String pathTestClassWithInjectedField = "com/societegenerale/aut/main/ClassWithInjectedField.class";

	@BeforeEach
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoAutowiredFieldTest().execute(pathTestClassWithAutowiredField, new TestSpecificScopeProvider(),
					emptySet());
		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(ClassWithAutowiredField.class.getName())
				.hasMessageContaining(NO_AUTOWIRED_FIELD_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoAutowiredFieldTest().execute(pathTestClassWithInjectedField,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();
	}

}
