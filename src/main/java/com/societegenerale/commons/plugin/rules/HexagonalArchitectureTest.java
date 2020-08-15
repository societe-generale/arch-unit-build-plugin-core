package com.societegenerale.commons.plugin.rules;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static java.util.stream.Collectors.toList;

/**
 * Hexagonal architecture is a way to arrange our code to enforce concerns are separated : we want to make sure that core domain code is not polluted by infrastructure code.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)">Hexagonal architecture on Wikipedia(software)</a>
 */
public class HexagonalArchitectureTest implements ArchRuleTest  {

    protected static final String WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE = "When following hexagonal architecture, ";
    private static final String DOMAIN = "..domain..";

    private Log log;

    private static String[] allowedPackageInDomain= {DOMAIN,
            "java..",
            "javax.validation..",
            "org.slf4j..",
            "lombok..",
            "org.apache.commons.."};

    private final String FORBIDDEN_SUFFIX_DTO=".*Dto$";
    private final String FORBIDDEN_SUFFIX_VO=".*Vo$";

    //removing the trailing ".." in package names, as we're going to use that list in a "startsWith" comparison
    private static List<String> allowedPackageInDomainPrefix = Arrays.asList(allowedPackageInDomain).stream().map(p -> p.replace("..","")).collect(toList());

    public HexagonalArchitectureTest(Log log){
        this.log=log;
    }

    private DescribedPredicate<JavaAnnotation> invalidAnnotations = new DescribedPredicate<JavaAnnotation>("invalid annotations, that don't belong to authorized packages") {
        @Override
        public boolean apply(JavaAnnotation annotation) {

            String annotationName = annotation.getRawType().getPackage().getName();

            log.info("testing "+ annotationName +"...");

            Optional<String> allowedPackagePrefixThatMatched=allowedPackageInDomainPrefix.stream().
                    filter(allowedPackagePrefix -> annotationName.startsWith(allowedPackagePrefix)).
                    findFirst();

            if(allowedPackagePrefixThatMatched.isPresent()){
                log.info( annotationName +" starts with "+allowedPackagePrefixThatMatched.get()+", which is an allowed prefix");
                return false;
            }
            else{
                log.warn(annotationName +" starts with none of the allowed prefixes");
                return true;
            }

        }
    };

    protected static ArchCondition<JavaClass> notHaveAnameEndingBy_ignoringCase(String forbiddenSuffixRegexp) {

        return new ArchCondition<JavaClass>("not have a name with that suffix") {

            Pattern pattern = Pattern.compile(forbiddenSuffixRegexp, Pattern.CASE_INSENSITIVE);

            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {

                if(pattern.matcher(javaClass.getSimpleName()).matches()){
                    events.add(SimpleConditionEvent.violated(javaClass, "class matched pattern "+forbiddenSuffixRegexp+" (ignoring case)"
                            +" - class: "+javaClass.getName()));
                }


            }
        };
    }


    @Override
    public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

        classes().that().resideInAPackage(DOMAIN)
                .should(notHaveAnameEndingBy_ignoringCase(FORBIDDEN_SUFFIX_DTO))
                .andShould(notHaveAnameEndingBy_ignoringCase(FORBIDDEN_SUFFIX_VO))
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE + "DTO / VO classes shouldn't be located in domain, as they are not business oriented")
                .check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(), packagePath,excludedPaths));

        noClasses().that().resideInAPackage(DOMAIN)
                .should().accessClassesThat().resideInAPackage("..infrastructure..")
                .orShould().accessClassesThat().resideInAPackage("..config..")
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE + "domain classes should not know about infrastructure or config code")
                .check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(), packagePath, excludedPaths));


        noClasses().that().resideInAPackage("..infrastructure..")
                .should().accessClassesThat().resideInAPackage("..config..")
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE+"infrastructure classes should not know about config code")
                .check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(), packagePath, excludedPaths));


        classes().that().resideInAPackage(DOMAIN)
                .should().onlyAccessClassesThat().resideInAnyPackage(allowedPackageInDomain)
                .andShould().notBeAnnotatedWith(invalidAnnotations)
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE + "domain classes should use only a limited set of core libraries, ie no external framework")
                .check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(), packagePath, excludedPaths));
    }


}
