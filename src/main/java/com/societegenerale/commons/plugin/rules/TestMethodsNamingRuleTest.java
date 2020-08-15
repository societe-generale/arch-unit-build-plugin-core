package com.societegenerale.commons.plugin.rules;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

/**
 * This rule raises an issue when a test method name does not match the provided
 * regular expression.
 * 
 * @see <a href=
 *      "https://rules.sonarsource.com/java/tag/convention/RSPEC-3578">Test
 *      Methods should comply with a naming convention.</a>
 * 
 * @see <a href= "https://dzone.com/articles/7-popular-unit-test-naming">7
 *      popular unit test naming</a>
 * 
 * @see <a href=
 *      "https://stackoverflow.com/questions/155436/unit-test-naming-best-practices">unit
 *      test naming best practices</a>
 * 
 */

public class TestMethodsNamingRuleTest implements ArchRuleTest {

	private static final String TEST_METHODS_NAMING_REGEX = "^(test|should)[A-Z][a-zA-Z0-9]*$";

	private static final Pattern TEST_METHODS_NAMING_PATTERN = Pattern.compile(TEST_METHODS_NAMING_REGEX);

	private static Matcher matcher;

	public static final String TEST_METHODS_VIOLATION_MESSAGE = "Test methods should comply with a naming convention. Method name has to start with prefixes \"test\" or \"should\" ";

	@Override
	public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		methods().that().areAnnotatedWith(Test.class).should(respectNamingConvention()).check(
				ArchUtils.importAllClassesInPackage( scopePathProvider.getTestClassesPath(), packagePath, excludedPaths));

	}

	private ArchCondition<JavaMethod> respectNamingConvention() {

		return new ArchCondition<JavaMethod>("comply with a naming convention") {

			@Override

			public void check(JavaMethod method, ConditionEvents events) {

				if (isInCorrect(method)) {

					events.add(SimpleConditionEvent.violated(method, TEST_METHODS_VIOLATION_MESSAGE + " - class: "
							+ method.getClass().getSimpleName() + " - method: " + method.getName()));
				}

			}

			private boolean isInCorrect(JavaMethod javaMethod) {

				matcher = TEST_METHODS_NAMING_PATTERN.matcher(javaMethod.getName());

				return !matcher.matches();

			}

		};

	}

}
