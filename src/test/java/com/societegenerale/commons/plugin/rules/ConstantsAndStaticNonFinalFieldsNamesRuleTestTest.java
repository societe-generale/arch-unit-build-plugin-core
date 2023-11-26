package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.societegenerale.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesRuleTest.CONSTANTS_VIOLATION_MESSAGE;
import static com.societegenerale.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesRuleTest.ENUM_CONSTANTS_VIOLATION_MESSAGE;
import static com.societegenerale.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesRuleTest.STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConstantsAndStaticNonFinalFieldsNamesRuleTestTest {

	private String pathClassWithConstantNamesNotWrittenCorrectly = "com/societegenerale/aut/main/ClassWithConstantNamesNotWrittenCorrectly.class";

	private String pathClassWithConstantNamesWrittenCorrectly = "com/societegenerale/aut/main/ClassWithConstantNamesWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsNotWrittenCorrectly = "com/societegenerale/aut/main/ClassWithStaticNonFinalFieldsNotWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsWrittenCorrectly = "com/societegenerale/aut/main/ClassWithStaticNonFinalFieldsWrittenCorrectly.class";

	private String pathEnumWithValuesNotWrittenCorrectly = "com/societegenerale/aut/main/EnumWithValuesNotWrittenCorrectly.class";

	private String pathEnumWithValuesWrittenCorrectly = "com/societegenerale/aut/main/EnumWithValuesWrittenCorrectly.class";

	@BeforeEach
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

	private void assertExceptionIsThrown(String packagePath, String violationMessage) {

		assertThatThrownBy(() -> {
			new ConstantsAndStaticNonFinalFieldsNamesRuleTest().execute(packagePath, new TestSpecificScopeProvider(), emptySet());
		}).hasMessageContaining(violationMessage);

	}

	private void assertNoExceptionIsThrown(String path) {

		assertThatCode(() -> new ConstantsAndStaticNonFinalFieldsNamesRuleTest().execute(path, new TestSpecificScopeProvider(), emptySet()))
				.doesNotThrowAnyException();

	}

}
