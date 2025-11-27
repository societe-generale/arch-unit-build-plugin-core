package com.societegenerale.aut.main;

import jakarta.inject.Inject;

public class ClassWithInjectedField {

    @Inject
    Object someDummyInjectedStuff;


    public ClassWithInjectedField() {

        //we should use constructor injection instead

    }
}
