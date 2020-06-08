package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.NoJodaTimeRuleTest.NO_JODA_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.aut.test.ObjectWithJodaTimeReferences;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class NoJodaTimeRuleTestTest {

	private String pathObjectWithJava8TimeLib = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithJava8TimeLib.class";

	private String pathObjectWithJodaTimeReferences = "./target/aut-target/test-classes/com/societegenerale/aut/test/ObjectWithJodaTimeReferences.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldCatchViolationsInStaticBlocksAndMemberFields() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoJodaTimeRuleTest().execute(pathObjectWithJodaTimeReferences, new DefaultScopePathProvider(),
					emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageContaining("was violated (2 times)")
				.hasMessageContaining("ObjectWithJodaTimeReferences - field name: jodaDatTime")
				.hasMessageContaining("ObjectWithJodaTimeReferences - line: 17");

		assertThat(validationExceptionThrown).hasMessageStartingWith("Architecture Violation")
				.hasMessageContaining(ObjectWithJodaTimeReferences.class.getName())
				.hasMessageContaining(NO_JODA_VIOLATION_MESSAGE);
	}

	@Test(expected = Throwable.class)
	public void shouldThrowNOJODAViolation() {
		new NoJodaTimeRuleTest().execute(pathObjectWithJodaTimeReferences, new DefaultScopePathProvider(), emptySet());
	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoJodaTimeRuleTest().execute(pathObjectWithJava8TimeLib,
				new DefaultScopePathProvider(), emptySet())).doesNotThrowAnyException();

	}
}
