package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import java.util.Collection;
import java.util.regex.Pattern;

import org.junit.Test;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

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

	public static final String TEST_METHODS_VIOLATION_MESSAGE = "Test methods should comply with a naming convention. Method name has to start with prefixes \"test\" or \"should\" ";

	@Override
	public void execute(String path, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		methods().that().areAnnotatedWith(Test.class).and(dontRespectNamingConvention).should(respectNamingConvention())
				.check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath(),
						excludedPaths));

	}

	private DescribedPredicate<JavaMethod> dontRespectNamingConvention = new DescribedPredicate<JavaMethod>(
			"don't respect naming convention") {
		@Override
		public boolean apply(JavaMethod javaMethod) {

			return !TEST_METHODS_NAMING_PATTERN.matcher(javaMethod.getName()).matches();

		}

	};

	private ArchCondition<JavaMethod> respectNamingConvention() {

		return new ArchCondition<JavaMethod>("comply with a naming convention") {

			@Override

			public void check(JavaMethod method, ConditionEvents events) {

				events.add(SimpleConditionEvent.violated(method, TEST_METHODS_VIOLATION_MESSAGE + " - class: "
						+ method.getClass().getSimpleName() + " - method: " + method.getName()));

			}

		};

	}

}
