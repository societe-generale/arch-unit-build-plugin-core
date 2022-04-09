package com.societegenerale.aut.main;

public class ClassWithConstantNamesWrittenCorrectly {

	public static final String NAME = "QUANTUM";

	private static final int NUMBER_FIVE = 5;

	private static final long NUMBER_SIX_LONG = 6l;

	// serialVersionUID is the exception : the only one accepted, as per https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html
	static final long serialVersionUID = 42L;

}
