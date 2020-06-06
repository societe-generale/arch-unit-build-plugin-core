package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;

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

	private List<Class<?>> allDirectOrIndirectInnerClasses = new ArrayList<>();

	@Override
	public void execute(String path, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		classes().that(haveAMethodAnnotatedWithTest).should(respectNamingConvention()).check(
				ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath(), excludedPaths));

	}

	private DescribedPredicate<JavaClass> haveAMethodAnnotatedWithTest = new DescribedPredicate<JavaClass>(
			"have a method annotated with @Test") {
		@Override
		public boolean apply(JavaClass input) {

			return isTestClass(input);

		}

		private boolean isTestClass(JavaClass input) {

			/*
			 * Checking if a method annotated with @Test is directly present in a root
			 * class. If true, the root class is a test class. If not, we should continue to
			 * search for @Test method
			 */

			if (isThereAtLeastOneMethodAnnotedWithTest(input)) {

				return true;

			} else {

				getAllInnerClasses(input.reflect());

				// Converting Inner Classes into JavaClasses to use Arch Unit API

				JavaClasses javaInnerClasses = new ClassFileImporter().importClasses(allDirectOrIndirectInnerClasses);

				Set<JavaClass> classesWhereTestIsPresentAtLeastOneTime = javaInnerClasses.stream()
						.filter(this::isThereAtLeastOneMethodAnnotedWithTest).collect(Collectors.toSet());

				if (classesWhereTestIsPresentAtLeastOneTime.isEmpty()) {

					return false;

				} else {

					return true;
				}

			}

		}

		// Getting All Inner Classes

		private void getAllInnerClasses(Class<?> clazz) {

			List<Class<?>> listInnerClasses = Arrays.asList(clazz.getDeclaredClasses());

			allDirectOrIndirectInnerClasses.addAll(listInnerClasses);

			// listInnerClasses.parallelStream().forEachOrdered(innerClass ->
			// getAllInnerClasses(innerClass));

			for (Class<?> a : listInnerClasses) {

				getAllInnerClasses(a);

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
