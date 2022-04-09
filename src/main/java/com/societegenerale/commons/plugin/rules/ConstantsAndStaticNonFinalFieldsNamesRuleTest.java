package com.societegenerale.commons.plugin.rules;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaEnumConstant;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

/**
 * 
 * Constants and Enum values have to be written in uppercase.
 *
 * Fields that are static and not final should not use constants naming.
 *
 * It's possible to add underscore but not at the beginning or the end of the
 * name.
 * 
 * @see <a href= "https://rules.sonarsource.com/java/RSPEC-115">Constant names
 *      should comply with a naming convention</a>
 * @see <a href=
 *      "https://rules.sonarsource.com/java/tag/convention/RSPEC-3008">Static
 *      non-final field names should comply with a naming convention</a>
 * 
 */

public class ConstantsAndStaticNonFinalFieldsNamesRuleTest implements ArchRuleTest {

	private static final String CONSTANTS_REGEX = "^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$";

	private static final String STATIC_NON_FINAL_FIELDS_REGEX = "^[a-z][a-zA-Z0-9]*$";

	private static final Pattern CONSTANTS_PATTERN = Pattern.compile(CONSTANTS_REGEX);

	private static final Pattern STATIC_NON_FINAL_FIELDS_PATTERN = Pattern.compile(STATIC_NON_FINAL_FIELDS_REGEX);

	private static Matcher matcher;

	public static final String CONSTANTS_VIOLATION_MESSAGE = "Constants have to be written in uppercase. It's possible to add underscore but not at the beginning or the end of the name.";

	public static final String STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE = "Static non-final fields should be in upperCase and/or use underscore";

	public static final String ENUM_CONSTANTS_VIOLATION_MESSAGE = "Enum constants have to be written in uppercase. It's possible to add underscore but not at the beginning or the end of the name.";

	@Override
	public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		fields().that().areDeclaredInClassesThat().areNotEnums().and().areStatic().and().areFinal().and(areNotSerialVersionUID)
				.should(beInUpperCaseAndUseUnderscore())
				.allowEmptyShould(true)
				.check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(),packagePath,excludedPaths));

		fields().that().areDeclaredInClassesThat().areNotEnums().and().areStatic().and().areNotFinal()
				.should(notBeInUpperCaseAndUseUnderscore())
				.allowEmptyShould(true)
				.check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(),packagePath,excludedPaths));

		classes().that().areEnums().should(haveConstantsInUpperCaseAndUseUnderscore())
				.allowEmptyShould(true)
				.check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(),packagePath, excludedPaths));

	}

	DescribedPredicate<JavaField> areNotSerialVersionUID =
			new DescribedPredicate<JavaField>("are not serialVersionUID"){
				@Override
				public boolean apply(JavaField input) {

					return !(input.getName().equals("serialVersionUID") );
				}
			};

	protected static ArchCondition<JavaField> beInUpperCaseAndUseUnderscore() {

		return new ArchCondition<JavaField>("be In UpperCase And Use Underscore") {

			@Override
			public void check(JavaField javaField, ConditionEvents events) {

				if (isInCorrect(javaField)) {

					events.add(SimpleConditionEvent.violated(javaField, CONSTANTS_VIOLATION_MESSAGE + " - class: "
							+ javaField.getOwner().getName() + " - field name: " + javaField.getName()));

				}

			}

			private boolean isInCorrect(JavaField field) {

				matcher = CONSTANTS_PATTERN.matcher(field.getName());
				return !matcher.matches();

			}

		};
	}

	protected static ArchCondition<JavaField> notBeInUpperCaseAndUseUnderscore() {

		return new ArchCondition<JavaField>("not Be In UpperCase And Use Underscore") {

			@Override
			public void check(JavaField javaField, ConditionEvents events) {

				if (isIncorrect(javaField)) {

					events.add(SimpleConditionEvent.violated(javaField, STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE
							+ " - class: " + javaField.getOwner().getName() + " - field name: " + javaField.getName()));

				}

			}

			private boolean isIncorrect(JavaField field) {

				matcher = STATIC_NON_FINAL_FIELDS_PATTERN.matcher(field.getName());
				return !matcher.matches();

			}

		};
	}

	protected static ArchCondition<JavaClass> haveConstantsInUpperCaseAndUseUnderscore() {

		return new ArchCondition<JavaClass>("have constants in UpperCase And Use Underscore") {
			@Override
			public void check(JavaClass item, ConditionEvents events) {

				item.getEnumConstants().stream().filter(this::isInCorrectEnums)
						.forEach(field -> events.add(
								SimpleConditionEvent.violated(field, ENUM_CONSTANTS_VIOLATION_MESSAGE + " - class: "
										+ field.getDeclaringClass().getName() + " - field name: " + field.name())));

			}

			private boolean isInCorrectEnums(JavaEnumConstant enumConstant) {

				matcher = CONSTANTS_PATTERN.matcher(enumConstant.name());
				return !matcher.matches();

			}

		};
	}

}
