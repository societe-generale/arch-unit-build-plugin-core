package com.societegenerale.commons.plugin.service;

import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.model.ConfigurableRule;
import com.societegenerale.commons.plugin.model.RootClassFolder;
import com.societegenerale.commons.plugin.model.Rules;
import com.societegenerale.commons.plugin.rules.ArchRuleTest;
import com.societegenerale.commons.plugin.service.InvokableRules.InvocationResult;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClasses;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.societegenerale.commons.plugin.utils.ReflectionUtils.loadClassWithContextClassLoader;
import static java.lang.System.lineSeparator;

public class RuleInvokerService {
    private static final String EXECUTE_METHOD_NAME = "execute";

    private Log log;

    private ArchUtils archUtils;

    private List<ScopePathProvider> scopePathProviders;

    private Collection<String> excludedPaths;

    public RuleInvokerService(Log log) {
        this(log, new DefaultScopePathProvider(), List.of(), null);
    }

    public RuleInvokerService(Log log, ScopePathProvider scopePathProvider) {
        this(log, scopePathProvider, List.of(), null);
    }

    public RuleInvokerService(Log log, ScopePathProvider scopePathProvider, Collection<String> excludedPaths, String projectBuildDir) {
        this(log, List.of(scopePathProvider), excludedPaths, projectBuildDir);
    }

    public RuleInvokerService(Log log, List<ScopePathProvider> scopePathProviders, Collection<String> excludedPaths, String projectBuildDir) {
        this.log = log;
        this.archUtils = new ArchUtils(log);
        this.scopePathProviders = scopePathProviders;
        this.excludedPaths = new ExcludedPathsPreProcessor().processExcludedPaths(log, projectBuildDir, excludedPaths);
    }

    public String invokeRules(Rules rules)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        StringBuilder errorListBuilder = new StringBuilder();

        for (String rule : rules.getPreConfiguredRules()) {
            String errorMessage = invokePreConfiguredRule(rule);
            errorListBuilder.append(prepareErrorMessageForRuleFailures(rule, errorMessage));
        }

        for (ConfigurableRule rule : rules.getConfigurableRules()) {
            String errorMessage = invokeConfigurableRules(rule);
            errorListBuilder.append(prepareErrorMessageForRuleFailures(rule.getRule(), errorMessage));
        }

        return errorListBuilder.toString();

    }

    private String invokePreConfiguredRule(String ruleClassName)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> ruleClass = loadClassWithContextClassLoader(ruleClassName);

        ArchRuleTest ruleToExecute;

        try {
            //sometimes, rules need to log - if they do, they should provide a constructor that accepts a Log...
            ruleToExecute = (ArchRuleTest) ruleClass.getConstructor(Log.class).newInstance(log);
        } catch (NoSuchMethodException e) {
            //.. otherwise, we use the default constructor with no param
            ruleToExecute = (ArchRuleTest) ruleClass.newInstance();
        }

        String errorMessage = "";
        try {
            Method method = ruleClass.getDeclaredMethod(EXECUTE_METHOD_NAME, String.class, ScopePathProvider.class, Collection.class);
            // preConfiguredRule only apply for single module
            method.invoke(ruleToExecute, "", scopePathProviders.get(0), excludedPaths);
        } catch (ReflectiveOperationException re) {
            errorMessage = re.getCause().toString();
        }
        return errorMessage;
    }

    private String invokeConfigurableRules(ConfigurableRule rule) {
        if (rule.isSkip()) {
            if (log.isInfoEnabled()) {
                log.info("Skipping rule " + rule.getRule());
            }
            return "";
        }

        InvokableRules invokableRules = InvokableRules.of(rule.getRule(), rule.getChecks(), log);

        List<String> fullPathFromRootToPackages = getPackageNameOnWhichToApplyRules(rule);

        log.info("invoking ConfigurableRule " + rule + " on " + fullPathFromRootToPackages);
        JavaClasses classes = ArchUtils.importAllClassesInPackages(new RootClassFolder(""), fullPathFromRootToPackages, excludedPaths);

        InvocationResult result = invokableRules.invokeOn(classes);
        return result.getMessage();
    }

    private List<String> getPackageNameOnWhichToApplyRules(ConfigurableRule rule) {
        List<String> packageNames = new ArrayList<>();

        if (rule.getApplyOn() != null) {
            for (ScopePathProvider scopePathProvider : scopePathProviders) {
                StringBuilder packageNameBuilder = new StringBuilder();

                if (rule.getApplyOn().getScope() != null && "test".equals(rule.getApplyOn().getScope())) {
                    packageNameBuilder.append(scopePathProvider.getTestClassesPath().getValue());
                } else {
                    packageNameBuilder.append(scopePathProvider.getMainClassesPath().getValue());
                }

                if (!packageNameBuilder.toString().endsWith("/")) {
                    packageNameBuilder.append("/");
                }

                if (rule.getApplyOn().getPackageName() != null) {
                    packageNameBuilder.append(DotsToSlashesReplacer.replace(rule.getApplyOn().getPackageName()));
                }

                packageNames.add(packageNameBuilder.toString());
            }
        }

        return packageNames;
    }


    private String prepareErrorMessageForRuleFailures(String rule, String errorMessage) {

        StringBuilder errorBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(errorMessage)) {
            errorBuilder
                    .append("Rule Violated - ").append(rule).append(lineSeparator())
                    .append(errorMessage)
                    .append(lineSeparator());
        }
        return errorBuilder.toString();
    }
}
