package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.service.ScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.*;
import java.util.regex.Pattern;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static java.util.stream.Collectors.toList;

/**
 * Hexagonal architecture is a way to arrange our code to enforce concerns are separated : we want to make sure that core domain code is not polluted by infrastructure code.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)">Hexagonal architecture on Wikipedia(software)</a>
 */
public class HexagonalArchitectureTest implements ArchRuleTest  {

    protected static final String WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE = "When following hexagonal architecture, ";
    private static final String DOMAIN = "..domain..";
    private static final String INFRA = "..infrastructure..";


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


    private static ArchCondition<JavaClass> notHavePublicMethodsOtherThanTheOnesDefinedInInterface() {

        return new ArchCondition<JavaClass>("not have public methods apart from the ones defined in the interface") {

            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {

                Set<JavaClass> implementedInterfaces = javaClass.getInterfaces();

                System.out.println("analysing "+javaClass.getFullName()+"...");

                if(implementedInterfaces.isEmpty()){
                    System.out.println("\t --> no interface");
                    return ;
                }

                for(JavaClass thisInterface : implementedInterfaces){

                    System.out.println("\t --> has interface "+thisInterface.getFullName()+"..");
                    System.out.println("\t --> "+thisInterface.toString());
                    System.out.println("\t --> code unit size : "+thisInterface.getCodeUnits().size());

                    Set<JavaMethod> methodsFromDomainInterface = thisInterface.getAllMethods();

                    System.out.println("\t\t --> nb of methods in the interface :  "+methodsFromDomainInterface.size());

                    if(thisInterface.getPackage().getName().contains("domain")){

                        List<JavaMethod> publicMethodsForInspectedClass = javaClass.getMethods().stream().filter(m -> m.getModifiers().contains(JavaModifier.PUBLIC)).collect(toList());

                        List<JavaMethod> methodsThatMatchedFromInterface=new ArrayList<>();

                        for(JavaMethod methodFromInterface : methodsFromDomainInterface){

                            for(JavaMethod publicMethodForInspectedClass : publicMethodsForInspectedClass){

                                if(publicMethodForInspectedClass.getName().equals(methodFromInterface.getName())){
                                    methodsThatMatchedFromInterface.add(publicMethodForInspectedClass);
                                    break;
                                }
                            }
                        }

                        publicMethodsForInspectedClass.removeAll(methodsThatMatchedFromInterface);

                        if(!publicMethodsForInspectedClass.isEmpty()){

                            for(JavaMethod publicMethodThatShouldNotBeThere : publicMethodsForInspectedClass) {



                                events.add(SimpleConditionEvent.violated(javaClass, "Classes should not have public methods that are not part of the interface\n"
                                        + " - class: " + javaClass.getName()+"\n"
                                        + " - method name: " + publicMethodThatShouldNotBeThere.getName()));
                            }

                        }

                    }

                }

            }
        };
    }

    @Override
    public void execute(String path, ScopePathProvider scopePathProvider) {



        System.out.println("TESTING FOR PATH : "+path);

        JavaClasses classesToInspect = ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath());

        System.out.println("NB of classes : "+classesToInspect.size());

        Optional<JavaClass> debug = classesToInspect.stream().filter(c -> c.getFullName().equals("de.gefa.gop.service.infrastructure.service.PartnerConfigurationServiceImpl")).findFirst();

        if(debug.isPresent()) {

            JavaClass partnerConfigurationServiceImpl =debug.get();

            System.out.println("interfaces found for partnerConfigurationServiceImpl : ");
            partnerConfigurationServiceImpl.getAllInterfaces().stream().forEach(i -> {
                System.out.println("\t - "+i.getName()+" - "+i.isInterface());

                try {
                    Object interfaceLoadedDynamically=Class.forName(i.getName());

                    System.out.println("interface is loadable : "+interfaceLoadedDynamically);
                } catch (ClassNotFoundException e) {
                    System.out.println("interface is NOT loadable");
                    e.printStackTrace();
                }

            });

            System.out.println("how many are interfaces : " + partnerConfigurationServiceImpl.getInterfaces().stream().filter(c -> c.isInterface()).count());




        }

        classes().that().resideInAPackage(INFRA)
                .should(notHavePublicMethodsOtherThanTheOnesDefinedInInterface())
                .because(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE + "infrastructure classes implementing domain interface should not have other public methods")
                .check(ArchUtils.importAllClassesInPackage(path, scopePathProvider.getMainClassesPath()));
    }



}
