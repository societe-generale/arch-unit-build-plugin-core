package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.ConstantNamesRuleTest.CONSTANTS_VIOLATION_MESSAGE;
import static com.societegenerale.commons.plugin.rules.ConstantNamesRuleTest.ENUM_CONSTANTS_VIOLATION_MESSAGE;
import static com.societegenerale.commons.plugin.rules.ConstantNamesRuleTest.STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.societegenerale.aut.main.ClassWithConstantNamesNotWrittenCorrectly;
import com.societegenerale.aut.main.ClassWithConstantNamesWrittenCorrectly;
import com.societegenerale.aut.main.ClassWithStaticNonFinalFieldsNotWrittenCorrectly;
import com.societegenerale.aut.main.ClassWithStaticNonFinalFieldsWrittenCorrectly;
import com.societegenerale.aut.main.EnumWithValuesNotWrittenCorrectly;
import com.societegenerale.aut.main.EnumWithValuesWrittenCorrectly;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public class ConstantNamesRuleTestTest {

	@Test
	public void shouldThrowViolations() {

		assertExceptionIsThrownFor(ClassWithConstantNamesNotWrittenCorrectly.class);

	}

	@Test
	public void shouldNotThrowAnyViolation() {

		assertNoExceptionIsThrownFor(ClassWithConstantNamesWrittenCorrectly.class);

	}

	@Test
	public void shouldThrowViolationsStaticNonFinalFields() {

		assertExceptionIsThrownForStaticNonFinalFields(ClassWithStaticNonFinalFieldsNotWrittenCorrectly.class);

	}

	@Test
	public void shouldNotThrowAnyViolationsStaticNonFinalFields() {

		assertNoExceptionIsThrownForStaticNonFinalFields(ClassWithStaticNonFinalFieldsWrittenCorrectly.class);

	}

	@Test
	public void shouldThrowViolationsEnums() {

		assertExceptionIsThrownForEnums(EnumWithValuesNotWrittenCorrectly.class);

	}

	@Test
	public void shouldNotThrowAnyViolationEnums() {

		assertNoExceptionIsThrownForEnums(EnumWithValuesWrittenCorrectly.class);

	}

	private void assertExceptionIsThrownFor(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatThrownBy(() -> {
			fields().that().areStatic().and().areFinal().should(ConstantNamesRuleTest.beInUpperCaseAndUseUnderscore())
					.check(classToTest);
		}).hasMessageContaining(clazz.getName()).hasMessageContaining(CONSTANTS_VIOLATION_MESSAGE);

	}

	private void assertNoExceptionIsThrownFor(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatCode(() -> fields().that().areStatic().and().areFinal()
				.should(ConstantNamesRuleTest.beInUpperCaseAndUseUnderscore()).check(classToTest))
						.doesNotThrowAnyException();

	}

	private void assertExceptionIsThrownForStaticNonFinalFields(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatThrownBy(() -> {
			fields().that().areStatic().and().areNotFinal()
					.should(ConstantNamesRuleTest.notBeInUpperCaseAndUseUnderscore()).check(classToTest);
		}).hasMessageContaining(clazz.getName()).hasMessageContaining(STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE);

	}

	private void assertNoExceptionIsThrownForStaticNonFinalFields(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatCode(() -> fields().that().areStatic().and().areNotFinal()
				.should(ConstantNamesRuleTest.notBeInUpperCaseAndUseUnderscore()).check(classToTest))
						.doesNotThrowAnyException();

	}

	private void assertExceptionIsThrownForEnums(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatThrownBy(() -> {
			classes().that().areEnums().should(ConstantNamesRuleTest.haveConstantsInUpperCaseAndUseUnderscore())
					.check(classToTest);
		}).hasMessageContaining(clazz.getName()).hasMessageContaining(ENUM_CONSTANTS_VIOLATION_MESSAGE);

	}

	private void assertNoExceptionIsThrownForEnums(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatCode(() -> classes().that().areEnums()
				.should(ConstantNamesRuleTest.haveConstantsInUpperCaseAndUseUnderscore()).check(classToTest))
						.doesNotThrowAnyException();

	}

}
