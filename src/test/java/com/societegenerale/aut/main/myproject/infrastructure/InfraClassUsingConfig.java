package com.societegenerale.aut.main.myproject.infrastructure;

import com.societegenerale.aut.main.myproject.config.ConfigClass;

public class InfraClassUsingConfig {

    public InfraClassUsingConfig() {

        ConfigClass configClassThatShouldNotBeHere=new ConfigClass();

    }
}
