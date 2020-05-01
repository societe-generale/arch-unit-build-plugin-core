package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesRuleTest.CONSTANTS_VIOLATION_MESSAGE;
import static com.societegenerale.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesRuleTest.ENUM_CONSTANTS_VIOLATION_MESSAGE;
import static com.societegenerale.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesRuleTest.STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

public class ConstantsAndStaticNonFinalFieldsNamesRuleTestTest {

	private String pathClassWithConstantNamesNotWrittenCorrectly = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithConstantNamesNotWrittenCorrectly.class";

	private String pathClassWithConstantNamesWrittenCorrectly = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithConstantNamesWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsNotWrittenCorrectly = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithStaticNonFinalFieldsNotWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsWrittenCorrectly = "./target/aut-target/test-classes/com/societegenerale/aut/test/ClassWithStaticNonFinalFieldsWrittenCorrectly.class";

	private String pathEnumWithValuesNotWrittenCorrectly = "./target/aut-target/test-classes/com/societegenerale/aut/test/EnumWithValuesNotWrittenCorrectly.class";

	private String pathEnumWithValuesWrittenCorrectly = "./target/aut-target/test-classes/com/societegenerale/aut/test/EnumWithValuesWrittenCorrectly.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolationsConstants() {

		assertExceptionIsThrown(pathClassWithConstantNamesNotWrittenCorrectly, CONSTANTS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationConstants() {

		assertNoExceptionIsThrown(pathClassWithConstantNamesWrittenCorrectly);

	}

	@Test
	public void shouldThrowViolationsStaticNonFinalFields() {

		assertExceptionIsThrown(pathClassWithStaticNonFinalFieldsNotWrittenCorrectly,
				STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationStaticNonFinalFields() {

		assertNoExceptionIsThrown(pathClassWithStaticNonFinalFieldsWrittenCorrectly);

	}

	@Test
	public void shouldThrowViolationsEnums() {

		assertExceptionIsThrown(pathEnumWithValuesNotWrittenCorrectly, ENUM_CONSTANTS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationEnums() {

		assertNoExceptionIsThrown(pathEnumWithValuesWrittenCorrectly);

	}

	private void assertExceptionIsThrown(String path, String violationMessage) {

		assertThatThrownBy(() -> {
			new ConstantsAndStaticNonFinalFieldsNamesRuleTest().execute(path, new DefaultScopePathProvider(), emptySet());
		}).hasMessageContaining(violationMessage);

	}

	private void assertNoExceptionIsThrown(String path) {

		assertThatCode(() -> new ConstantsAndStaticNonFinalFieldsNamesRuleTest().execute(path, new DefaultScopePathProvider(), emptySet()))
				.doesNotThrowAnyException();

	}

}
