package com.societegenerale.aut.main.myproject.infrastructure;

import org.springframework.core.SpringProperties;

public class InfraClassUsingSpring {

    public InfraClassUsingSpring() {

        //some dummy code using Spring..
        SpringProperties.getProperty("someProperty");

    }
}
