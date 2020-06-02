package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import java.util.Collection;
import java.util.regex.Pattern;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * 
 * We regularly see date fields that are not typed properly. However, it's quite
 * easy to identify them by name, because they usually finish by "Date", like
 * <b>String startDate<b/> . We need a rule that will look at all the class's
 * String fields.
 */

public class StringFieldsThatAreActuallyDatesRuleTest implements ArchRuleTest {

	private static final String STRING_FIELDS_THAT_ARE_ACTUALLY_DATES_NAMING_REGEX = "^[a-z][a-zA-Z0-9]*Date$";

	private static final Pattern STRING_FIELDS_THAT_ARE_ACTUALLY_DATES_PATTERN = Pattern
			.compile(STRING_FIELDS_THAT_ARE_ACTUALLY_DATES_NAMING_REGEX);

	@Override
	public void execute(String path, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		fields().that().haveRawType(String.class).and(areFinishedByDate).should(beDates()).check(
				ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath(), excludedPaths));

	}

	private DescribedPredicate<JavaField> areFinishedByDate = new DescribedPredicate<JavaField>(
			"are finished by \"Date\"") {
		@Override
		public boolean apply(JavaField field) {

			return STRING_FIELDS_THAT_ARE_ACTUALLY_DATES_PATTERN.matcher(field.getName()).matches();
		}

	};

	private ArchCondition<JavaField> beDates() {

		return new ArchCondition<JavaField>("be Dates") {

			@Override
			public void check(JavaField field, ConditionEvents events) {
				events.add(SimpleConditionEvent.violated(field, "field \"" + field.getName()
						+ "\" is a String, but it seems to be a Date. If it really is a Date, please change its type accordingly. Strong typing helps write more meaningful code. - class: "
						+ field.getOwner().getName()));

			}

		};

	}

}
