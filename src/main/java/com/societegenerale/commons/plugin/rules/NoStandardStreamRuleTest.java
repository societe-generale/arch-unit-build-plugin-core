package com.societegenerale.commons.plugin.rules;

import java.util.Collection;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;


public class NoStandardStreamRuleTest implements ArchRuleTest {

  private static final ArchCondition<JavaClass> notUseStandardStream = ACCESS_STANDARD_STREAMS;

  @Override
  public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {
    noClasses().should(notUseStandardStream)
            .allowEmptyShould(true)
            .check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(),packagePath,  excludedPaths));
  }

  public static ArchCondition<JavaClass> getNotUseStandardStream(){
    return ACCESS_STANDARD_STREAMS;
  }
}
