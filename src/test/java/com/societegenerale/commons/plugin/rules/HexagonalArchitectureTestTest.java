package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

import static com.societegenerale.commons.plugin.rules.HexagonalArchitectureTest.WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE;
import static org.assertj.core.api.Assertions.*;


public class HexagonalArchitectureTestTest {


    private String pathForDomainClassUsingSpring = "./target/aut-target/classes/com/societegenerale/aut/main/myproject/domain/DomainClassUsingSpring.class";

    private String pathForDomainClassAnnotatedWithJson = "./target/aut-target/classes/com/societegenerale/aut/main/myproject/domain/DomainClassAnnotatedWithJson.class";

    private String pathForDomainClassAnnotatedWithLombok = "./target/aut-target/classes/com/societegenerale/aut/main/myproject/domain/DomainClassAnnotatedWithLombok.class";


    private String pathForInfraClassUsingSpring = "./target/aut-target/classes/com/societegenerale/aut/main/myproject/infrastructure/InfraClassUsingSpring.class";

    private String pathForInfraClassUsingConfig = "./target/aut-target/classes/com/societegenerale/aut/main/myproject/infrastructure/InfraClassUsingConfig.class";

    @Before
    public void setup(){
        //in the normal lifecycle, ArchUtils is instantiated, which enables a static field there to be initialized
        ArchUtils archUtils=new ArchUtils(new SilentLog());
    }


    @Test
    public void domainClassUsingSpringFrameworkShouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            new HexagonalArchitectureTest().execute(pathForDomainClassUsingSpring, new DefaultScopePathProvider());

        });

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (1 times)")
                .hasMessageContaining("ClassUsingSpring")
                .hasMessageContaining(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE)
                .hasMessageContaining("domain classes should use only a limited set of core libraries");

    }

    @Test
    public void domainClassAnnotatedWithJacksonShouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            new HexagonalArchitectureTest().execute(pathForDomainClassAnnotatedWithJson, new DefaultScopePathProvider());

        });

        assertThat(validationExceptionThrown).as("a violation should have been raised").isNotNull();

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (1 times)")
                .hasMessageContaining("ClassAnnotatedWithJson")
                .hasMessageContaining(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE)
                .hasMessageContaining("domain classes should use only a limited set of core libraries");

    }

    @Test
    public void domainClassAnnotatedWithLombokShould_Not_ThrowViolations(){

        assertThatCode(() -> new HexagonalArchitectureTest().execute(pathForDomainClassAnnotatedWithLombok, new DefaultScopePathProvider())).doesNotThrowAnyException();

    }

    @Test
    public void infraClassUsingSpringFrameworkShould_Not_ThrowViolations() {

        assertThatCode(() -> new NoPublicFieldRuleTest().execute(pathForInfraClassUsingSpring, new DefaultScopePathProvider()))
                .doesNotThrowAnyException();

    }

    @Test
    public void infraClassUsingConfigShouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            new HexagonalArchitectureTest().execute(pathForInfraClassUsingConfig, new DefaultScopePathProvider());

        });

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (1 times)")
                .hasMessageContaining("ClassUsingConfig")
                .hasMessageContaining(WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE)
                .hasMessageContaining("infrastructure classes should not know about config code");

    }

}