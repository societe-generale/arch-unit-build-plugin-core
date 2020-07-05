package com.societegenerale.aut.main;

import javax.inject.Inject;


public class ClassWithInjectedField {

    @Inject
    Object someDummyInjectedStuff;


    public ClassWithInjectedField() {

        //we should use constructor injection instead

    }
}
