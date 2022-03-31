package com.societegenerale.aut.main;

import com.google.common.base.Suppliers;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ObjectWithLambdasReturningListsInside {

	private final Supplier<List<Object>> someCache;


	public ObjectWithLambdasReturningListsInside(Supplier<List<Object>> someSupplier) {

		someCache =  Suppliers.memoizeWithExpiration(
				()-> someSupplier.get(),
				6, TimeUnit.HOURS);

	}


}
