package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.library.GeneralCodingRules;

/**
 * It's important to throw specific exceptions than generic exceptions in order
 * to get the real source of what's going wrong. Thus, it's easy to find
 * solutions.
 * 
 * To make you better understand, these 2 links will help you :
 * 
 * 
 * 
 * @see <a href=
 *      "https://wiki.c2.com/?DontThrowGenericExceptions">DontThrowGenericExceptions</a>
 * 
 * 
 * @see <a href=
 *      "https://stackoverflow.com/questions/7959461/throwing-generic-exception-discouraged">throwing-generic-exception-discouraged</a>
 * 
 * 
 */

public class NoGenericExceptionRuleTest implements ArchRuleTest {

	public static final String NO_GENERIC_EXCEPTION_VIOLATION_MESSAGE = "it's important to throw specific exceptions than generic exceptions in order to get the real source of what's going wrong. Thus, it's easy to find solutions.";

	@Override
	public void execute(String path, ScopePathProvider scopePathProvider) {

		GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.because(NO_GENERIC_EXCEPTION_VIOLATION_MESSAGE)
				.check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath()));

	}

}
