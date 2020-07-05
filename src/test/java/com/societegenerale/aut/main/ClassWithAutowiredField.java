package com.societegenerale.aut.main;

import org.springframework.beans.factory.annotation.Autowired;

public class ClassWithAutowiredField {

    @Autowired
    Object someDummyInjectedStuff;


    public ClassWithAutowiredField() {

        //we should use constructor injection instead

    }
}
