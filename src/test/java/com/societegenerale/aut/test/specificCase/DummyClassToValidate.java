package com.societegenerale.aut.test.specificCase;

import org.joda.time.JodaTimePermission;

public class DummyClassToValidate {

    //a Joda time field to trigger a violation from com.tngtech.archunit.library.GeneralCodingRules
    private JodaTimePermission anyJodaTimeObject;

}
