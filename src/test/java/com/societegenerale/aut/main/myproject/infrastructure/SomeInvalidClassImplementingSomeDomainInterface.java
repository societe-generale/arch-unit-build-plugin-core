package com.societegenerale.aut.main.myproject.infrastructure;

import com.societegenerale.aut.main.myproject.domain.SomeDomainInterface;

public class SomeInvalidClassImplementingSomeDomainInterface implements SomeDomainInterface {

    public SomeInvalidClassImplementingSomeDomainInterface() {
    }

    @Override
    public void someLogicToImplement() {
        publicMethodsAreNotOK();
    }

    public void publicMethodsAreNotOK(){

    }
}
