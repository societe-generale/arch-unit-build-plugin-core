package com.societegenerale.aut.main;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.util.List;
import java.util.Set;

public class ObjectWithProperlyAnnotatedMethodsReturningCollections {

	@jakarta.annotation.Nonnull
	public List returningANullListWithJakartaNonnullAnnotation(){
		return emptyList();
	}

	@jakarta.annotation.Nonnull
	public Set returningANullSetWithJakartaNonnullAnnotation(){
		return emptySet();
	}

	@javax.annotation.Nonnull
	public List returningANullListWithJavaxNonnullAnnotation(){
		return emptyList();
	}

	@javax.annotation.Nonnull
	public Set returningANullSetWithJavaxNonnullAnnotation(){
		return emptySet();
	}
}
