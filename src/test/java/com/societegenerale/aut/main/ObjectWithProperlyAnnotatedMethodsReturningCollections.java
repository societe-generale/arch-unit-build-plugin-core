package com.societegenerale.aut.main;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public class ObjectWithProperlyAnnotatedMethodsReturningCollections {

	@Nonnull
	public List returningANullList(){
		return emptyList();
	}

	@Nonnull
	public Set returningANullSet(){
		return emptySet();
	}

}
