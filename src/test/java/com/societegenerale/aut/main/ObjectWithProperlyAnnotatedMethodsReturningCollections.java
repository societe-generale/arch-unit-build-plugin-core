package com.societegenerale.aut.main;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

public class ObjectWithProperlyAnnotatedMethodsReturningCollections {

	@Nonnull
	public List returningANullList() {
		return emptyList();
	}

	@Nonnull
	public Set returningANullSet() {
		return emptySet();
	}

}
