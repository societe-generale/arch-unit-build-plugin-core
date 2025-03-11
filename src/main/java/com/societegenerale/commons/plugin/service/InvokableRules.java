package com.societegenerale.commons.plugin.service;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.utils.ReflectionUtils;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.ArchTests;
import com.tngtech.archunit.lang.ArchRule;

import static com.societegenerale.commons.plugin.utils.ReflectionUtils.getValue;
import static com.societegenerale.commons.plugin.utils.ReflectionUtils.invoke;
import static com.societegenerale.commons.plugin.utils.ReflectionUtils.loadClassWithContextClassLoader;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

class InvokableRules {
    private final Set<Field> archRuleFields;
    private final Set<Method> archRuleMethods;

    private final Log log;

    private InvokableRules(String rulesClassName, List<String> ruleChecks, Log log) {

        this.log=log;

        Class<?> definedRulesClass = loadClassWithContextClassLoader(rulesClassName);

        Set<Class<?>> rulesClasses = getAllClassesWhichAreArchTests(definedRulesClass);
        rulesClasses.add(definedRulesClass);
        Set<Field> allFieldsWhichAreArchRules = new HashSet<>();
        Set<Method> allMethodsWhichAreArchRules = new HashSet<>();
        for (Class<?> rulesClass : rulesClasses) {
            allFieldsWhichAreArchRules.addAll(getAllFieldsWhichAreArchRules(rulesClass.getDeclaredFields()));
            allMethodsWhichAreArchRules.addAll(getAllMethodsWhichAreArchRules(rulesClass.getDeclaredMethods()));
        }
        validateRuleChecks(definedRulesClass, Sets.union(allMethodsWhichAreArchRules, allFieldsWhichAreArchRules), ruleChecks);

        Predicate<String> isChosenCheck = ruleChecks.isEmpty() ? check -> true : ruleChecks::contains;

        archRuleFields = filterNames(allFieldsWhichAreArchRules, isChosenCheck);
        archRuleMethods = filterNames(allMethodsWhichAreArchRules, isChosenCheck);

        if(log.isInfoEnabled()) {
            logBuiltInvokableRules(rulesClassName);
        }
    }

    private void logBuiltInvokableRules(String rulesClassName) {

        log.info("just built "+rulesClassName+" : ");

        log.info(archRuleFields.size()+ " field rules loaded ");
        archRuleFields.stream().forEach(a -> log.info(a.toString()));

        log.info(archRuleMethods.size()+ " method rules loaded");
        archRuleMethods.stream().forEach(a -> log.info(a.toString()));

    }

    private void validateRuleChecks(Class<?> rulesLocation, Set<? extends Member> allFieldsAndMethods, Collection<String> ruleChecks) {
        Set<String> allFieldAndMethodNames = allFieldsAndMethods.stream().map(Member::getName).collect(toSet());
        Set<String> illegalChecks = Sets.difference(ImmutableSet.copyOf(ruleChecks), allFieldAndMethodNames);

        if (!illegalChecks.isEmpty()) {
            throw new IllegalChecksConfigurationException(rulesLocation, illegalChecks);
        }
    }

    private <M extends Member> Set<M> filterNames(Set<M> members, Predicate<String> namePredicate) {
        return members.stream()
                .filter(member -> namePredicate.test(member.getName()))
                .collect(toSet());
    }

    private Set<Method> getAllMethodsWhichAreArchRules(Method[] methods) {
        return stream(methods)
                .filter(m -> m.getParameterCount() == 1 && JavaClasses.class.isAssignableFrom(m.getParameterTypes()[0]))
                .collect(toSet());
    }

    private Set<Field> getAllFieldsWhichAreArchRules(Field[] fields) {
        return stream(fields)
                .filter(f -> ArchRule.class.isAssignableFrom(f.getType()))
                .collect(toSet());
    }

    private Set<Class<?>> getAllClassesWhichAreArchTests(Class<?> startClass) {
        Set<Class<?>> allClassesWhichAreArchTests = new HashSet<>();
        Deque<Class<?>> stack = new ArrayDeque<>();
        stack.push(startClass);
        while (!stack.isEmpty()) {
            Class<?> currentClass = stack.pop();
            stream(currentClass.getDeclaredFields())
                    .filter(f -> ArchTests.class.isAssignableFrom(f.getType()))
                    .map(f -> getValue(f, null))
                    .map(ArchTests.class::cast)
                    .map(ArchTests::getDefinitionLocation)
                    .forEach(childClass -> {
                        allClassesWhichAreArchTests.add(childClass);
                        stack.push(childClass);
                    });
        }
        return allClassesWhichAreArchTests;
    }

    InvocationResult invokeOn(JavaClasses importedClasses) {

        if(log.isInfoEnabled()) {
            log.info("applying rules on "+importedClasses.size()+" classe(s). To see the details, enable debug logs");

            if(log.isDebugEnabled()) {
                importedClasses.stream().forEach(c -> log.debug(c.getName()));
            }
        }

        InvocationResult result = new InvocationResult();
        for (Method method : archRuleMethods) {
            checkForFailure(() -> invoke(method, null, importedClasses))
                    .ifPresent(result::add);
        }
        for (Field field : archRuleFields) {
            ArchRule rule = getValue(field, null);
            checkForFailure(() -> rule.check(importedClasses))
                    .ifPresent(result::add);
        }
        return result;
    }

    private Optional<String> checkForFailure(Runnable runnable) {
        try {
            runnable.run();
            return Optional.empty();
        } catch (RuntimeException | AssertionError e) {
            return Optional.of(e.getMessage());
        }
    }

    static InvokableRules of(String rulesClassName, List<String> checks, Log log) {
        return new InvokableRules(rulesClassName, checks, log);
    }

    static class InvocationResult {
        private final List<String> violations = new ArrayList<>();

        private void add(String violationMessage) {
            violations.add(violationMessage);
        }

        String getMessage() {
            return violations.stream().collect(joining(lineSeparator()));
        }
    }
}
