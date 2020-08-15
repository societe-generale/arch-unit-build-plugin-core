package com.societegenerale.commons.plugin.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.model.ConfigurableRule;
import com.societegenerale.commons.plugin.model.RootClassFolder;
import com.societegenerale.commons.plugin.model.Rules;
import com.societegenerale.commons.plugin.rules.ArchRuleTest;
import com.societegenerale.commons.plugin.service.InvokableRules.InvocationResult;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClasses;
import org.apache.commons.lang3.StringUtils;

import static com.societegenerale.commons.plugin.utils.ReflectionUtils.loadClassWithContextClassLoader;
import static java.lang.System.lineSeparator;
import static java.util.Collections.emptySet;

public class RuleInvokerService {
    private static final String EXECUTE_METHOD_NAME = "execute";

    private Log log;

    private ArchUtils archUtils;

    private ScopePathProvider scopePathProvider=new DefaultScopePathProvider();

    private Collection<String> excludedPaths= emptySet();

    public RuleInvokerService(Log log) {
        this.log=log;
        archUtils =new ArchUtils(log);
    }

    public RuleInvokerService(Log log, ScopePathProvider scopePathProvider) {
        this.log=log;
        archUtils =new ArchUtils(log);

        this.scopePathProvider=scopePathProvider;
    }

    public RuleInvokerService(Log log, ScopePathProvider scopePathProvider,Collection<String> excludedPaths) {
        this.log=log;
        archUtils =new ArchUtils(log);

        this.scopePathProvider=scopePathProvider;
        this.excludedPaths=excludedPaths;
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

        try{
            //sometimes, rules need to log - if they do, they should provide a constructor that accepts a Log...
            ruleToExecute= (ArchRuleTest) ruleClass.getConstructor(Log.class).newInstance(log);
        }
        catch(NoSuchMethodException e){
            //.. otherwise, we use the default constructor with no param
            ruleToExecute= (ArchRuleTest)ruleClass.newInstance();
        }

        String errorMessage = "";
        try {
            Method method = ruleClass.getDeclaredMethod(EXECUTE_METHOD_NAME, String.class, ScopePathProvider.class, Collection.class);
            method.invoke(ruleToExecute, "", scopePathProvider,excludedPaths);
        } catch (ReflectiveOperationException re) {
            errorMessage = re.getCause().toString();
        }
        return errorMessage;
    }

    private String invokeConfigurableRules(ConfigurableRule rule) {
        if(rule.isSkip()) {
            if(log.isInfoEnabled()) {
                log.info("Skipping rule " + rule.getRule());
            }
            return "";
        }

        InvokableRules invokableRules = InvokableRules.of(rule.getRule(), rule.getChecks(),log);

        String fullPathFromRootTopackage = getPackageNameOnWhichToApplyRules(rule);

        log.info("invoking ConfigurableRule "+rule.toString()+" on "+fullPathFromRootTopackage);
        JavaClasses classes = archUtils.importAllClassesInPackage(new RootClassFolder(""), fullPathFromRootTopackage,excludedPaths);

        InvocationResult result = invokableRules.invokeOn(classes);
        return result.getMessage();
    }

    private String getPackageNameOnWhichToApplyRules(ConfigurableRule rule) {

        StringBuilder packageNameBuilder = new StringBuilder();

        if (rule.getApplyOn() != null) {
            if (rule.getApplyOn().getScope() != null && "test".equals(rule.getApplyOn().getScope())) {
                packageNameBuilder.append(scopePathProvider.getTestClassesPath().getValue());
            }
            else{
                packageNameBuilder.append(scopePathProvider.getMainClassesPath().getValue());
            }

            if(!packageNameBuilder.toString().endsWith("/")){
                packageNameBuilder.append("/");
            }

            if (rule.getApplyOn().getPackageName() != null) {
                packageNameBuilder.append(rule.getApplyOn().getPackageName());
            }

        }

        return DotsToSlashesReplacer.replace(packageNameBuilder.toString());
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
