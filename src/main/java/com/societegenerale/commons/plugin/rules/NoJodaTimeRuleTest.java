package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import java.util.Collection;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * for Java8 projects, Joda time deprecated, but a lot of people still use it
 * out of years of habit. This rule will catch such instances, and remind
 * developers they should use the Java 8 classes.
 *
 * @see <a href="https://www.joda.org/joda-time/">on Joda time website</a> :
 *      <i>from Java SE 8 onwards, users are asked to migrate to java.time
 *      (JSR-310) - a core part of the JDK which replaces this project</i>
 */
public class NoJodaTimeRuleTest implements ArchRuleTest {

	private static final String JODATIME_PACKAGE_PREFIX = "org.joda";

	protected static final String NO_JODA_VIOLATION_MESSAGE = "Use Java8 Date API instead of Joda library";

	@Override
	public void execute(String path, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {
		classes().should(notUseJodaTime()).check(
				ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath(), excludedPaths));
	}

	protected static ArchCondition<JavaClass> notUseJodaTime() {

		return new ArchCondition<JavaClass>("not use Joda time ") {
			@Override
			public void check(JavaClass item, ConditionEvents events) {

				item.getAllFields().stream().filter(this::isJodaTimeField)
						.forEach(field -> events.add(SimpleConditionEvent.violated(field, NO_JODA_VIOLATION_MESSAGE
								+ " - class: " + field.getOwner().getName() + " - field name: " + field.getName())));

				item.getCodeUnits().stream().filter(codeUnit -> codeUnit instanceof JavaMethod)
						.flatMap(method -> method.getMethodCallsFromSelf().stream())
						.filter(method -> method instanceof JavaMethodCall)
						.filter(this::isMethodUsingJodaTimeInternally)
						.forEach(methodCall -> events.add(SimpleConditionEvent.violated(methodCall.getOriginOwner(),
								NO_JODA_VIOLATION_MESSAGE + " - class: " + methodCall.getOriginOwner().getName()
										+ " - line: " + methodCall.getLineNumber())));

			}

			private boolean isJodaTimeField(JavaField field) {
				return field.getRawType().getName().startsWith(JODATIME_PACKAGE_PREFIX);
			}

			private boolean isMethodUsingJodaTimeInternally(JavaMethodCall javaMethodCall) {
				return javaMethodCall.getTarget().getFullName().startsWith(JODATIME_PACKAGE_PREFIX);
			}

		};
	}

}
