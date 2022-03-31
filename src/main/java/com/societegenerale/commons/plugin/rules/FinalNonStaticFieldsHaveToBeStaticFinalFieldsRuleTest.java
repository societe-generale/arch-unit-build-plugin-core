package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import java.util.Collection;

/**
 * 
 * Final Non Static Fields have to be Static Final Fields
 * 
 * @see <a href=
 *      "https://stackoverflow.com/questions/35095483/why-should-data-fields-be-static-and-final">why-should-data-fields-be-static-and-final</a>
 * 
 * @see <a href=
 *      "https://rules.sonarsource.com/java/tag/convention/RSPEC-1170">Public
 *      constants and fields initialized at declaration should be "static final"
 *      rather than merely "final"</a>
 */

public class FinalNonStaticFieldsHaveToBeStaticFinalFieldsRuleTest implements ArchRuleTest {

	public static final String FINAL_NON_STATIC_FIELDS_VIOLATION_MESSAGE = "you are duplicating its value for every instance of the class, uselessly increasing the amount of memory required to execute the application.";

	@Override
	public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

		fields().that().areFinal().and().areNotStatic().should().beStatic().andShould().beFinal()
				.because(FINAL_NON_STATIC_FIELDS_VIOLATION_MESSAGE)
				.allowEmptyShould(true)
				.check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(),packagePath, excludedPaths));

	}

}
