package com.societegenerale.aut.main.myproject.infrastructure;

import com.societegenerale.aut.main.myproject.domain.SomeDomainInterface;

public class SomeValidClassImplementingSomeDomainInterface implements SomeDomainInterface {


    @Override
    public void someLogicToImplement() {
        privateMethodsAreOK();
    }

    private void privateMethodsAreOK(){

    }
}
