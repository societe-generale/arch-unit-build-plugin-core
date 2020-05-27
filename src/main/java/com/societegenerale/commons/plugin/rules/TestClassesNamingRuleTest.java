package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * This rule raises an issue when a test class name does not match the provided
 * regular expression.
 * 
 * @see <a href=
 *      "https://rules.sonarsource.com/java/tag/convention/RSPEC-3577">Test
 *      Classes should comply with a naming convention.</a>
 * 
 * 
 */

public class TestClassesNamingRuleTest implements ArchRuleTest {

	private static final String TEST_CLASSES_NAMING_REGEX = "^((Test|IT)[a-zA-Z0-9]+|[A-Z][a-zA-Z0-9]*(Test|Tests|IT|TestCase|ITCase))$";

	private static final Pattern TEST_CLASSES_NAMING_PATTERN = Pattern.compile(TEST_CLASSES_NAMING_REGEX);

	private static Matcher matcher;

	public static final String TEST_CLASS_VIOLATION_MESSAGE = "Test classes should comply with a naming convention. Class name  has to end with one of those suffixes : \"Test\" ; \"Tests\" ; \"IT\" ; \"TestCase\" ; \"ITCase\"";

	@Override
	public void execute(String path, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		classes().that(haveAMethodAnnotatedWithTest).should(respectNamingConvention()).check(
				ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath(), excludedPaths));

	}

	private DescribedPredicate<JavaClass> haveAMethodAnnotatedWithTest = new DescribedPredicate<JavaClass>(
			"have a method annotated with @Test") {
		@Override
		public boolean apply(JavaClass input) {

			return !input.getMethods().stream().filter(this::isAnnotedWithTest).collect(Collectors.toSet()).isEmpty();
		}

		private boolean isAnnotedWithTest(JavaMethod method) {

			return method.isAnnotatedWith(Test.class);
		}
	};

	private ArchCondition<JavaClass> respectNamingConvention() {

		return new ArchCondition<JavaClass>("comply with a naming convention") {

			@Override

			public void check(JavaClass item, ConditionEvents events) {

				if (isInCorrect(item)) {

					events.add(SimpleConditionEvent.violated(item,
							TEST_CLASS_VIOLATION_MESSAGE + " - class: " + item.getName()));
				}

			}

			private boolean isInCorrect(JavaClass javaClass) {

				matcher = TEST_CLASSES_NAMING_PATTERN.matcher(javaClass.getSimpleName());

				return !matcher.matches();

			}

		};

	}

}
