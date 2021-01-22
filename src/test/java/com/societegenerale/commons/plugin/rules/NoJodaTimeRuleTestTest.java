package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.main.ObjectWithJodaTimeReferences;
import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

import static com.societegenerale.commons.plugin.rules.NoJodaTimeRuleTest.NO_JODA_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoJodaTimeRuleTestTest {


    private String pathObjectWithJodaTimeReferences = "com/societegenerale/aut/main/ObjectWithJodaTimeReferences.class";
    private String pathObjectWithJava8Library = "com/societegenerale/aut/main/ObjectWithJava8TimeLib.class";

    @Before
    public void setup() {
        // in the normal lifecycle, ArchUtils is instantiated, which enables a static
        // field there to be initialized
        ArchUtils archUtils = new ArchUtils(new SilentLog());
    }

    @Test
    public void shouldCatchViolationsInStaticBlocksAndMemberFields() {

        Throwable validationExceptionThrown = catchThrowable(() -> {
            new NoJodaTimeRuleTest().execute(pathObjectWithJodaTimeReferences, new TestSpecificScopeProvider(), emptySet());
        });

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageContaining("was violated (3 times)")
                .hasMessageContaining("ObjectWithJodaTimeReferences - field name: jodaDatTime")
                .hasMessageContaining("ObjectWithJodaTimeReferences - method: DateTimeFormat.forPattern - line: 17")
                .hasMessageContaining("ObjectWithJodaTimeReferences - method: DateTimeFormatter.getParser - line: 17");

        assertThat(validationExceptionThrown).hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining(ObjectWithJodaTimeReferences.class.getName())
                .hasMessageContaining(NO_JODA_VIOLATION_MESSAGE);

    }

    @Test
    public void shouldNotThrowAnyViolation(){
        assertThatCode(
            () -> new NoJodaTimeRuleTest().execute(pathObjectWithJava8Library, new TestSpecificScopeProvider(), emptySet()))
            .doesNotThrowAnyException();
    }
}
