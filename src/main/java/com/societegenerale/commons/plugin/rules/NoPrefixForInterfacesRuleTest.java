package com.societegenerale.commons.plugin.rules;

import java.util.Collection;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


/**
 * Interfaces shouldn't be prefixed with "I" like it's the case in .Net
 *
 * @see  <a href="https://stackoverflow.com/a/2814831/3067542">this SO post for the rationale</a>
 */
public class NoPrefixForInterfacesRuleTest implements ArchRuleTest {

  protected static final String NO_PREFIX_INTERFACE_VIOLATION_MESSAGE = " : Interfaces shouldn't be prefixed with \"I\" - caller doesn't need to know it's an interface + this is a .Net convention";

  private final static Character upperCaseI = 'I';

  @Override
  public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

    classes().that().areInterfaces().should(notBePrefixed())
            .allowEmptyShould(true)
            .check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(), packagePath, excludedPaths));

  }

  public static ArchCondition<JavaClass> notBePrefixed() {

    return new ArchCondition<JavaClass>("not be prefixed with I - this is a .Net convention.") {
      @Override
      public void check(JavaClass item, ConditionEvents events) {

        Character firstCharacter = item.getSimpleName().charAt(0);
        char secondCharacter = item.getSimpleName().charAt(1);

        if (firstCharacter.equals(upperCaseI) && Character.isUpperCase(secondCharacter)) {
          events.add(SimpleConditionEvent.violated(item, item.getName() + NO_PREFIX_INTERFACE_VIOLATION_MESSAGE));
        }
      }
    };


  }


}
