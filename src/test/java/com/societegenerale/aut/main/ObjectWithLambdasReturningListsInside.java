package com.societegenerale.aut.main;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.base.Suppliers;

public class ObjectWithLambdasReturningListsInside {

	private final Supplier<List<Object>> someCache;

	public ObjectWithLambdasReturningListsInside(Supplier<List<Object>> someSupplier) {

		someCache =  Suppliers.memoizeWithExpiration(
				()-> someSupplier.get(),
				6, TimeUnit.HOURS);

	}

	@Nonnull
	public List<Object> getLIstOfObjects(){
		return someCache.get();
	}

}
