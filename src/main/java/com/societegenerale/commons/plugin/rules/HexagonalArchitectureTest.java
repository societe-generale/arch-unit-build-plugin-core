package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Hexagonal architecture is a way to arrange our code to enforce concerns are separated : we want to make sure that core domain code is not polluted by infrastructure code.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)">Hexagonal architecture on Wikipedia(software)</a>
 */
public class HexagonalArchitectureTest implements ArchRuleTest  {

    protected static final String WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE = "When following hexagonal architecture, ";
    private static final String DOMAIN = "..domain..";

    @Override
    public void execute(String path, ScopePathProvider scopePathProvider) {

        noClasses().that().resideInAPackage(DOMAIN)
                .should().accessClassesThat().resideInAPackage("..infrastructure..")
                .orShould().accessClassesThat().resideInAPackage("..config..")
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE + "domain classes should not know about infrastructure or config code")
                .check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath()));

        noClasses().that().resideInAPackage("..infrastructure..")
                .should().accessClassesThat().resideInAPackage("..config..")
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE+"infrastructure classes should not know about config code")
                .check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath()));

        classes().that().resideInAPackage(DOMAIN)
                .should().onlyAccessClassesThat().resideInAnyPackage(DOMAIN,
                "java..",
                "javax.validation..",
                "org.slf4j..",
                "org.projectlombok..",
                "org.apache.commons..")
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE + "domain classes should use only a limited set of core libraries, ie no external framework")
                .check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath()));
    }


}
