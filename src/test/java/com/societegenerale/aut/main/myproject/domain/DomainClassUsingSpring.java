package com.societegenerale.aut.main.myproject.domain;

import org.springframework.core.SpringProperties;

public class DomainClassUsingSpring {

    public DomainClassUsingSpring() {

        //some dummy code using Spring..
        SpringProperties.getProperty("someProperty");

    }
}
