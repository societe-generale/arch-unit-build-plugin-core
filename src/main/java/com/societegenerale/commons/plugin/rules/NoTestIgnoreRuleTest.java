package com.societegenerale.commons.plugin.rules;

import java.util.Collection;
import java.util.Optional;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.properties.HasAnnotations;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * This rule will make sure we don't have any test marked as @Ignore / @Disabled. Code is in source control, so we can always remove the test and add it back later, instead of having dozens of @Ignore tests piling up
 */
public class NoTestIgnoreRuleTest implements ArchRuleTest  {

  protected static final String NO_JUNIT_IGNORE_VIOLATION_MESSAGE = "Tests shouldn't been ignored";

  @Override
  public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {
    classes().should(notBeenIgnore()).check(ArchUtils.importAllClassesInPackage(scopePathProvider.getTestClassesPath(), packagePath, excludedPaths));
  }

  public static ArchCondition<JavaClass> notBeenIgnore() {

    return new ArchCondition<JavaClass>(NO_JUNIT_IGNORE_VIOLATION_MESSAGE) {

      @Override
      @SuppressWarnings("squid:S1166")
      public void check(JavaClass item, ConditionEvents events) {

        //class level checks
        String violationMessageAtClassLevel = item.getName() + ", at class level";

        //class level checks
        addViolationEvent(buildViolationIfAnnotationWithNoValueFound(item,Ignore.class,violationMessageAtClassLevel),events);
        addViolationEvent(buildViolationIfAnnotationWithNoValueFound(item,Disabled.class,violationMessageAtClassLevel),events);

        //method level checks
        for (JavaMethod method : item.getMethods()) {

          String violationMessageAtMethodLevel = item.getName() + " - " + method.getName() + ", at method level";

          addViolationEvent(buildViolationIfAnnotationWithNoValueFound(method,Ignore.class, violationMessageAtMethodLevel),events);
          addViolationEvent(buildViolationIfAnnotationWithNoValueFound(method,Disabled.class, violationMessageAtMethodLevel),events);
        }

      }

      private void addViolationEvent(Optional<ConditionEvent> violation, ConditionEvents events) {
          if(violation.isPresent()){
            events.add(violation.get());
          }
      }

      private Optional<ConditionEvent> buildViolationIfAnnotationWithNoValueFound(HasAnnotations item, Class annotation, String violationMessage) {

        try {
          if (item.getAnnotationOfType(annotation) != null)  {
            return Optional.of(SimpleConditionEvent.violated(item, violationMessage));
          }
        } catch (IllegalArgumentException e) {
          //if there's no Ignore annotation, IllegalArgument exception is thrown.
          //we swallow it, as it means there's no annotation at class level.
        }
        return Optional.empty();
      }

    };
  }

}
