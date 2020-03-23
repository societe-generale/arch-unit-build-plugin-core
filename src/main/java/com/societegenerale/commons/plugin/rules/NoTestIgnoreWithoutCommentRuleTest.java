package com.societegenerale.commons.plugin.rules;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
 * You may want to accept having tests marked as @Ignore / @Disabled, but only if a comment is provided explaining the reason - there's nothing worse than a test marked as @Ignore, with no indication as of why it has been ignored.
 */
public class NoTestIgnoreWithoutCommentRuleTest implements ArchRuleTest  {

    protected static final String NO_JUNIT_IGNORE_WITHOUT_COMMENT_VIOLATION_MESSAGE = "Tests shouldn't been ignored without providing a comment explaining why";

    public void execute(String path, ScopePathProvider scopePathProvider, Collection<String> excludedPaths)  {
        classes().should(notBeIgnoredWithoutAComment()).check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getTestClassesPath(),excludedPaths));
    }

    public static ArchCondition<JavaClass> notBeIgnoredWithoutAComment() {

        return new ArchCondition<JavaClass>(NO_JUNIT_IGNORE_WITHOUT_COMMENT_VIOLATION_MESSAGE) {

            @Override
            @SuppressWarnings("squid:S1166")
            public void check(JavaClass item, ConditionEvents events) {

                //class level checks
                String violationMessageAtClassLevel = item.getName() + ", at class level";

                addViolationEvent(buildViolationIfAnnotationWithNoValueFound(item,Ignore.class, violationMessageAtClassLevel),events);
                addViolationEvent(buildViolationIfAnnotationWithNoValueFound(item, Disabled.class, violationMessageAtClassLevel),events);

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
                    if (getAnnotationValue(item,annotation).isEmpty())  {
                        return Optional.of(SimpleConditionEvent.violated(item, violationMessage));
                    }
                } catch (IllegalArgumentException e) {
                    //if there's no Ignore annotation, IllegalArgument exception is thrown.
                    //we swallow it, as it means there's no annotation at class level.
                }
                catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    //this won't happen, as we use reflection on a type that we know has the method we are looking for
                }
                return Optional.empty();
            }

            private String getAnnotationValue(HasAnnotations annotatedObj, Class annotation)
                    throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

                Annotation ann = annotatedObj.getAnnotationOfType(annotation);
                Method valueMethod=ann.getClass().getDeclaredMethod("value");
                return (String)valueMethod.invoke(ann);
            }

        };
    }
}
