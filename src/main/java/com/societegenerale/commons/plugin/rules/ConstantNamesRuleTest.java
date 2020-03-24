package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaEnumConstant;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * 
 * Constant names and Enum values have to be written in uppercase.
 * 
 * It's possible to add underscore but not at the beginning or the end of the
 * name.
 * 
 * @see <a href= "https://rules.sonarsource.com/java/RSPEC-115">Constant names
 *      should comply with a naming convention</a>
 * 
 * 
 */

public class ConstantNamesRuleTest implements ArchRuleTest {

	private static Pattern pattern;

	private static Matcher matcher;

	private static String namePattern = "^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$";

	public static final String CONSTANT_NAMES_VIOLATION_MESSAGE = "Constant names have to be written in uppercase. It's possible to add underscore but not at the beginning or the end of the name.";

	public static final String ENUM_VALUES_VIOLATION_MESSAGE = "Enum values have to be written in uppercase. It's possible to add underscore but not at the beginning or the end of the name.";

	@Override
	public void execute(String path, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		fields().that().areFinal().should(beInUpperCaseAndUseUnderscoreIfNeeded())
				.because(CONSTANT_NAMES_VIOLATION_MESSAGE).check(ArchUtils.importAllClassesInPackage(path,
						scopePathProvider.getMainClassesPath(), excludedPaths));

		classes().that().areEnums().should(beInUpperCaseAndUseUnderscoreIfNeededEnums())
				.because(ENUM_VALUES_VIOLATION_MESSAGE).check(ArchUtils.importAllClassesInPackage(path,
						scopePathProvider.getMainClassesPath(), excludedPaths));
	}

	protected static ArchCondition<JavaField> beInUpperCaseAndUseUnderscoreIfNeeded() {

		return new ArchCondition<JavaField>("be In UpperCase And Use Underscore If Needed") {

			@Override
			public void check(JavaField javaField, ConditionEvents events) {

				if (isInCorrect(javaField)) {

					events.add(SimpleConditionEvent.violated(javaField, CONSTANT_NAMES_VIOLATION_MESSAGE + " - class: "
							+ javaField.getOwner().getName() + " - field name: " + javaField.getName()));

				}

			}

		};
	}

	protected static ArchCondition<JavaClass> beInUpperCaseAndUseUnderscoreIfNeededEnums() {

		return new ArchCondition<JavaClass>("have their values in UpperCase And Use Underscore If Needed") {
			@Override
			public void check(JavaClass item, ConditionEvents events) {

				item.getEnumConstants().stream().filter(ConstantNamesRuleTest::isInCorrectEnums)
						.forEach(field -> events
								.add(SimpleConditionEvent.violated(field, ENUM_VALUES_VIOLATION_MESSAGE + " - class: "
										+ field.getDeclaringClass().getName() + " - field name: " + field.name())));

			}

		};
	}

	private static boolean isInCorrect(JavaField field) {

		pattern = Pattern.compile(namePattern);
		matcher = pattern.matcher(field.getName());
		return !matcher.matches();

	}

	private static boolean isInCorrectEnums(JavaEnumConstant enumConstant) {

		pattern = Pattern.compile(namePattern);
		matcher = pattern.matcher(enumConstant.name());
		return !matcher.matches();

	}

}
