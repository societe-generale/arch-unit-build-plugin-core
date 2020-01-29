package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static java.util.stream.Collectors.toList;

/**
 * Hexagonal architecture is a way to arrange our code to enforce concerns are separated : we want to make sure that core domain code is not polluted by infrastructure code.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)">Hexagonal architecture on Wikipedia(software)</a>
 */
public class HexagonalArchitectureTest implements ArchRuleTest  {

    protected static final String WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE = "When following hexagonal architecture, ";
    private static final String DOMAIN = "..domain..";

    private static String[] allowedPackageInDomain= {DOMAIN,
            "java..",
            "javax.validation..",
            "org.slf4j..",
            "lombok..",
            "org.apache.commons.."};

    //removing the trailing ".." in package names, as we're going to use that list in a "startsWith" comparison
    private static List<String> allowedPackageInDomainPrefix = Arrays.asList(allowedPackageInDomain).stream().map(p -> p.replace("..","")).collect(toList());

    private DescribedPredicate<JavaAnnotation> invalidAnnotations = new DescribedPredicate<JavaAnnotation>("invalid annotations, that don't belong to authorized packages") {
        @Override
        public boolean apply(JavaAnnotation annotation) {

            String annotationName = annotation.getRawType().getPackage().getName();

            System.out.println("testing "+ annotationName +"...");

            Optional<String> allowedPackagePrefixThatMatched=allowedPackageInDomainPrefix.stream().
                    filter(allowedPackagePrefix -> annotationName.startsWith(allowedPackagePrefix)).
                    findFirst();

            if(allowedPackagePrefixThatMatched.isPresent()){
                System.out.println("INFO - "+ annotationName +" starts with "+allowedPackagePrefixThatMatched.get()+", which is an allowed prefix");
                return false;
            }
            else{
                System.out.println("ERROR - "+ annotationName +" starts with none of the allowed prefixes");
                return true;
            }

        }
    };

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
                .should().onlyAccessClassesThat().resideInAnyPackage(allowedPackageInDomain)
                .andShould().notBeAnnotatedWith(invalidAnnotations)
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE + "domain classes should use only a limited set of core libraries, ie no external framework")
                .check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath()));
    }


}
