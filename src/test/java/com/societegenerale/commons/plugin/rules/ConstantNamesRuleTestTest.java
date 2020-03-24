package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.ConstantNamesRuleTest.CONSTANT_NAMES_VIOLATION_MESSAGE;
import static com.societegenerale.commons.plugin.rules.ConstantNamesRuleTest.ENUM_VALUES_VIOLATION_MESSAGE;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.societegenerale.aut.main.ClassWithConstantNamesNotWrittenCorrectly;
import com.societegenerale.aut.main.ClassWithConstantNamesWrittenCorrectly;
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
	public void shouldThrowViolationsEnums() {

		assertExceptionIsThrownForEnums(EnumWithValuesNotWrittenCorrectly.class);

	}

	@Test
	public void shouldNotThrowAnyViolation() {

		assertNoExceptionIsThrownFor(ClassWithConstantNamesWrittenCorrectly.class);

	}

	@Test
	public void shouldNotThrowAnyViolationEnums() {

		assertNoExceptionIsThrownForEnums(EnumWithValuesWrittenCorrectly.class);

	}

	private void assertNoExceptionIsThrownForEnums(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatCode(() -> classes().that().areEnums()
				.should(ConstantNamesRuleTest.beInUpperCaseAndUseUnderscoreIfNeededEnums()).check(classToTest))
						.doesNotThrowAnyException();

	}

	private void assertExceptionIsThrownForEnums(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatThrownBy(() -> {
			classes().that().areEnums().should(ConstantNamesRuleTest.beInUpperCaseAndUseUnderscoreIfNeededEnums())
					.check(classToTest);
		}).hasMessageContaining(EnumWithValuesNotWrittenCorrectly.class.getName())
				.hasMessageContaining(ENUM_VALUES_VIOLATION_MESSAGE);

	}

	private void assertNoExceptionIsThrownFor(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatCode(() -> fields().that().areFinal()
				.should(ConstantNamesRuleTest.beInUpperCaseAndUseUnderscoreIfNeeded()).check(classToTest))
						.doesNotThrowAnyException();

	}

	private void assertExceptionIsThrownFor(Class clazz) {

		JavaClasses classToTest = new ClassFileImporter().importClasses(clazz);

		assertThatThrownBy(() -> {
			fields().that().areFinal().should(ConstantNamesRuleTest.beInUpperCaseAndUseUnderscoreIfNeeded())
					.check(classToTest);
		}).hasMessageContaining(ClassWithConstantNamesNotWrittenCorrectly.class.getName())
				.hasMessageContaining(CONSTANT_NAMES_VIOLATION_MESSAGE);

	}

}
