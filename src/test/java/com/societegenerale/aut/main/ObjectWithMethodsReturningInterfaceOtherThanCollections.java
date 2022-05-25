package com.societegenerale.aut.main;

import javax.annotation.Nonnull;

public class ObjectWithMethodsReturningInterfaceOtherThanCollections {

	@Nonnull
	public SomeImplementation someAttribute(){
		return null;
	}

	public Object someObject(){
		return null;
	}


	private class SomeImplementation implements InterfaceWithCorrectName{

	}
}
