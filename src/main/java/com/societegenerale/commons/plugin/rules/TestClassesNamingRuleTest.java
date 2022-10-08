package com.societegenerale.commons.plugin.rules;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

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

	public static final String TEST_CLASS_VIOLATION_MESSAGE = "Test classes should comply with a naming convention";

	@Override
	public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		classes().that(haveAMethodAnnotatedWithTest).should(respectNamingConvention())
				.allowEmptyShould(true)
				.check(ArchUtils.importAllClassesInPackage(scopePathProvider.getTestClassesPath(), packagePath, excludedPaths));

	}

	private final DescribedPredicate<JavaClass> haveAMethodAnnotatedWithTest = new DescribedPredicate<JavaClass>(
			"have a method annotated with @Test") {
		@Override
		public boolean test(JavaClass input) {

			return isTestClass(input);

		}

		private boolean isTestClass(JavaClass input) {

			/*
			 * 
			 * This code works here
			 * 
			 * 
			 * 
			 * public class ClassTestWithIncorrectName2 {
			 * 
			 * @Nested class PositiveCase {
			 * 
			 * @Test public void check() { }
			 * 
			 * }
			 * 
			 * }
			 * 
			 * ----------------------------------
			 * 
			 * But not there
			 * 
			 * public class ClassTestWithIncorrectName2 {
			 * 
			 * @Nested class PositiveCase {
			 * 
			 * @Nested class PositiveCase1 {
			 * 
			 * @Test public void check() { }
			 * 
			 * }
			 * 
			 * }
			 * 
			 * }
			 * 
			 * 
			 * ------
			 * 
			 * Recursion looks like the solution
			 * 
			 */

			// Getting Inner Classes using Java Core

			Class<?>[] innerClasses = input.reflect().getDeclaredClasses();

			if (innerClasses.length == 0) {
				return isThereAtLeastOneMethodAnnotedWithTest(input);

			} else {

				// Converting Inner Classes into JavaClasses to use Arch Unit API

				JavaClasses javaInnerClasses = new ClassFileImporter().importClasses(Arrays.asList(innerClasses));

				Set<JavaClass> testClasses = javaInnerClasses.stream()
						.filter(this::isThereAtLeastOneMethodAnnotedWithTest).collect(Collectors.toSet());

				return !testClasses.isEmpty();

			}

		}

		private boolean isThereAtLeastOneMethodAnnotedWithTest(JavaClass javaClass) {

			// Check if the list of methods annoted with @Test is not empty

			return !javaClass.getMethods().stream().filter(this::isAnnotedWithTest).collect(Collectors.toSet())
					.isEmpty();

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

				return !TEST_CLASSES_NAMING_PATTERN.matcher(javaClass.getSimpleName()).matches();

			}

		};

	}

}
