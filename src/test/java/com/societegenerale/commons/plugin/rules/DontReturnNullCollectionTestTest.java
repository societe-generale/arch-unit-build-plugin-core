package com.societegenerale.commons.plugin.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

public class DontReturnNullCollectionTestTest {

    String pathObjectWithAmethodReturningAnullList = "com/societegenerale/aut/main/ObjectWithMethodsReturningNullCollections.class";

    String pathProperlyAnnotatedObjectWithAmethodReturningAlist = "com/societegenerale/aut/main/ObjectWithProperlyAnnotatedMethodsReturningCollections.class";

    String pathObjectWithLambdasReturningListsInside = "com/societegenerale/aut/main/ObjectWithLambdasReturningListsInside.class";

    String pathObjectWithLombokBuilder = "com/societegenerale/aut/main/ObjectWithLombokBuilder.class$ObjectWithLombokBuilderBuilder";

    @Before
    public void setup(){
        //in the normal lifecycle, ArchUtils is instantiated, which enables a static field there to be initialized
        new ArchUtils(new SilentLog());
    }

    @Test
    public void shouldThrowViolations() {

        Throwable validationExceptionThrown = catchThrowable(() ->
            new DontReturnNullCollectionTest().execute(pathObjectWithAmethodReturningAnullList, new TestSpecificScopeProvider(), emptySet())
        );

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageContaining("was violated (2 times)");

    }

    @Test
    public void shouldNotThrowViolations() {

        assertThatCode(() -> new DontReturnNullCollectionTest().execute(pathProperlyAnnotatedObjectWithAmethodReturningAlist, new TestSpecificScopeProvider(),emptySet()))
                .doesNotThrowAnyException();

    }


    @Test
    public void shouldNotThrowViolationsOnLambdas() {

        assertThatCode(() -> new DontReturnNullCollectionTest()
            .execute(pathObjectWithLambdasReturningListsInside, new TestSpecificScopeProvider(),emptySet()))
            .doesNotThrowAnyException();

    }

    @Test
    public void shouldNotThrowViolationsOnClassesUsingLombokBuilder() {

        assertThatCode(() -> new DontReturnNullCollectionTest()
            .execute("com/societegenerale/aut/main/lombok_builder", new TestSpecificScopeProvider(),emptySet()))
            .doesNotThrowAnyException();

    }
}
